package io.github.malikzh.qchain.services;

import io.github.malikzh.qchain.models.Block;
import io.github.malikzh.qchain.repositories.BlockchainRepository;
import io.github.malikzh.qchain.requests.SyncRequest;
import io.github.malikzh.qchain.responses.SyncResponse;
import kz.gov.pki.kalkan.util.encoders.Base64;
import kz.gov.pki.kalkan.util.encoders.Hex;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import static io.github.malikzh.qchain.utils.Constants.HASH_SIZE;
import static io.github.malikzh.qchain.utils.Util.getRandomNumber;
import static io.github.malikzh.qchain.utils.Util.parseLocalDateTime;

/**
 * Сервис для синхронизации блокчейна.
 *
 * Работает это так:
 * - Для каждой ноды:
 *      1. Делаем GET /qchain/api/v1/blockchain/top запрос, получаем хэш последнего блока у удалённой ноды
 *      2. Если сервис не ответил, удаляем эту ноду у себя из списка пиров
 *      3. Если всё ок, ищем в своём блокчейне этот блок
 *      4. Если наш блокчейн длинее (то есть мы нашли блок по хэшу, то отправляем нашу версию)
 *      4.1 Отправляем с помощью метода POST /qchain/api/v1/sync
 *      5. Если удаленная нода прислала нам валидные блоки, которые длинее наших, то перезаписываем
 *
 *      Вроде ничего сложного :)
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class SyncService {
    private final BlockchainRepository blockchainRepository;
    private final BlockchainService blockchainService;

    /**
     * Метод синхронизации блоков (нода обращается к нам)
     *
     * @param remote
     * @return
     */
    public SyncResponse sync(SyncRequest remote) {
        byte[] remoteTop = Base64.decode(remote.getTop());
        byte[] localTop = blockchainRepository.getLastBlockHash();

        if (Objects.isNull(remoteTop) || remoteTop.length != HASH_SIZE) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid top hash");
        }

        // Если наш блокчейн пуст, мы не принимаем цепочки ни от кого
        if (Objects.isNull(localTop)) {
            throw new ResponseStatusException(HttpStatus.NOT_IMPLEMENTED, "Blockchain of this node is empty");
        }

        List<Block> blocks = new ArrayList<>();

        var hashesIsEqual = Arrays.equals(remoteTop, localTop);

        if (!hashesIsEqual) {
            // Сначала пытаемся сформировать наш список блоков для ноды
            var depth = blockchainRepository.getDepthTo(remoteTop);

            // Вытаскиваем блоки
            if (depth >=  0) {
                blocks = blockchainRepository.getBlocksFromTop(depth);
            }

            // Теперь пробуем обновить наши блоки
            var remoteBlocks = remote.getBlocks();

            if (Objects.nonNull(remoteBlocks) && remoteBlocks.size() > 0) {
                var prevHash = remoteBlocks.get(0).getPrevBlockHash();

                if (Objects.isNull(prevHash) || prevHash.length != HASH_SIZE) {
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid prevHash of first block");
                }

                // Находим место куда вставлять
                depth = blockchainRepository.getDepthTo(prevHash);

                var prevBlock = blockchainRepository.findBlock(prevHash);

                if (Objects.isNull(prevBlock)) {
                    throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                            "Не могу найти предыдущий блок");
                }

                if (depth >= 0) {
                    // Вставляем только в случае если блок с пира длинее чем наш, а если они равны, выбираем случайно
                    if (depth < remoteBlocks.size() || (depth == remoteBlocks.size() && getRandomNumber(0, 1) == 1)) {
                        // Проверяем блоки из запроса
                        if (!blockchainService.validate(prevHash, parseLocalDateTime(prevBlock.getTimestamp()), remoteBlocks)) {
                            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Получены некорректные блоки");
                        }

                        // Если всё ок, то обновляем наши блоки
                        for (Block block : remoteBlocks) {
                            var h = block.calculateHash();
                            log.info("Updating our block {} ", Hex.encodeStr(h));
                            blockchainRepository.save(h, block.toJson());
                        }
                    }
                }
            }
        }

        return SyncResponse.builder()
                .top(Base64.encodeStr(localTop))
                .blocks(blocks)
                .build();
    }
}
