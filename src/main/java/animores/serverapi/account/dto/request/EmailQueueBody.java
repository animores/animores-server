package animores.serverapi.account.dto.request;

public record EmailQueueBody(
        String email,
        String code
) {
}
