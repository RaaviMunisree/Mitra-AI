private final ChatClient chatClient;
private final RagRetrievalService retrievalService;

public RagGenerationService(
        ChatClient.Builder chatClientBuilder,
        RagRetrievalService retrievalService) {

    this.chatClient = chatClientBuilder.build();
    this.retrievalService = retrievalService;
}