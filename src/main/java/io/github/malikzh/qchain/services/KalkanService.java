package io.github.malikzh.qchain.services;

import io.github.malikzh.qchain.exceptions.InvalidSignatureException;
import kz.gov.pki.kalkan.jce.provider.KalkanProvider;
import kz.gov.pki.kalkan.xmldsig.KncaXS;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.xml.security.keys.KeyInfo;
import org.apache.xml.security.signature.XMLSignature;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.annotation.PostConstruct;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;
import java.security.Security;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;

/**
 * Сервис для работы с криптографией, проверки подписей, OCSP и т.д.
 */
@RequiredArgsConstructor
@Service
@Slf4j
public class KalkanService {

    private KalkanProvider provider = new KalkanProvider();

    /**
     * Полная проверка.
     *
     * Полная проверка работает так:
     * 1. Сначала проверяется сама подпись
     * 2. Проверяем сертификат на возможность использования
     * 3. Потом проверяется сертификат и его цепочка
     * 4. И после этого, происходит проверка в OCSP и CRL
     */
    public void validate(String xml) throws InvalidSignatureException {
        try {
            var signatures = getXmlSignatures(xml);

            if (signatures.isEmpty()) {
                throw new Exception("Подписей не найдено");
            }

            // Запускаем проверку подписей
            for (var signature : signatures) {
                KeyInfo keyInfo = signature.getKeyInfo();
                X509Certificate cert = keyInfo.getX509Certificate();

                // Проверяем саму подпись
                if (!signature.checkSignatureValue(cert)) {
                    throw new InvalidSignatureException(HttpStatus.BAD_REQUEST, "Ошибка проверки подписи");
                }

                // Проверяем сертификат на возможность использования
                // todo

                // Проверяем цепочку сертификатов
                // todo

                // Проверка на отозванность
                // todo
            }

        } catch (InvalidSignatureException e) {
            throw e;
        } catch (Exception e) {
            throw new InvalidSignatureException(HttpStatus.BAD_REQUEST, "Ошибка проверки подписи: " + e.getMessage());
        }
    }

    /**
     * Возвращает подписи XML
     *
     * @param xml
     * @return
     */
    private List<XMLSignature> getXmlSignatures(String xml) throws Exception {
        var signatures = new ArrayList<XMLSignature>();

        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        dbf.setNamespaceAware(true);
        DocumentBuilder documentBuilder = dbf.newDocumentBuilder();
        Document doc = documentBuilder.parse(new ByteArrayInputStream(xml.getBytes(StandardCharsets.UTF_8)));

        Element rootEl = (Element) doc.getFirstChild();

        NodeList list = rootEl.getElementsByTagName("ds:Signature");
        int length = list.getLength();
        for (int i = 0; i < length; i++) {
            Node sigNode = list.item(i);
            var sigElement = (Element) sigNode;

            if (sigElement == null) {
                continue;
            }

            signatures.add(new XMLSignature(sigElement, ""));
        }

        return signatures;
    }

    @PostConstruct
    private void initializeKalkan() {
        log.info("Initialize KalkanCrypt...");
        Security.addProvider(provider);
        KncaXS.loadXMLSecurity();
        log.info("KalkanCrypt initialized...");
    }
}
