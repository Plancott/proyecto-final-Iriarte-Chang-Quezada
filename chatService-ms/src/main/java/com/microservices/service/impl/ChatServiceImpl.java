package com.microservices.service.impl;

import com.microservices.client.OpenAiClient;
import com.microservices.model.dto.ChatRequestDto;
import com.microservices.model.dto.ChatResponseDto;
import com.microservices.model.dto.ProductDto;
import com.microservices.service.ChatService;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class ChatServiceImpl implements ChatService {
    private final OpenAiClient openAiClient;

    public ChatServiceImpl(OpenAiClient openAiClient) {
        this.openAiClient = openAiClient;
    }

    @Override
    public Mono<ChatResponseDto> recommend(ChatRequestDto request) {
        StringBuilder prompt = new StringBuilder();
        prompt.append("Eres un asistente que recomienda un producto en base al mensaje del usuario.\n");
        prompt.append("Mensaje del usuario: ").append(request.getMenssage()).append("\n");
        prompt.append("Lista de productos (id | nombre | descripcion | categoria | unitPrice):\n");
        request.getProducts().forEach(p -> {
            prompt.append(String.format("%d | %s | %s | %s | %.2f\n",
                    p.getId() == null ? 0 : p.getId(),
                    p.getName(),
                    p.getDescription() == null ? "" : p.getDescription(),
                    p.getCategory() == null ? "" : p.getCategory(),
                    p.getUnitPrice() == null ? 0.0 : p.getUnitPrice()));
        });

        prompt.append("\nInstrucciones:\n");
        prompt.append(" - Devuelve el ID del producto recomendado en la primera línea con el formato: ID=numero.\n");
        prompt.append(" - Luego redacta un único párrafo claro explicando por qué lo recomiendas.\n");
        prompt.append(" - No uses listas, numeraciones ni asteriscos.\n");
        prompt.append(" - Si no hay una recomendación clara, devuelve únicamente: ID=0 y explica brevemente por qué.\n");

        return openAiClient.askCompletion(prompt.toString())
                .map(aiText -> {
                    String cleanText = aiText.replaceAll("\\s+", " ").trim();

                    Pattern pattern = Pattern.compile("ID=(\\d+)");
                    Matcher matcher = pattern.matcher(cleanText);

                    ProductDto elegido = null;
                    if (matcher.find()) {
                        String idFound = matcher.group(1);

                        elegido = request.getProducts().stream()
                                .filter(p -> p.getId() != null && p.getId().toString().equals(idFound))
                                .findFirst()
                                .orElse(null);

                        cleanText = cleanText.replaceFirst("ID=\\d+\\.?\\s*", "").trim();
                    }

                    String decision = (elegido != null) ? "RECOMENDADO" : "NO_CLARO";

                    ChatResponseDto response = new ChatResponseDto();
                    response.setDecision(decision);
                    response.setProductRecommended(elegido);
                    response.setRawOpenAiText(cleanText);
                    return response;
                });
    }

}
