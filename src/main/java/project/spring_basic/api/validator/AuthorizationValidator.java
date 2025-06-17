package project.spring_basic.api.validator;

import java.nio.file.AccessDeniedException;

public class AuthorizationValidator {

    public static void validateUserAccess(Long pathId, Long sessionUserId) throws Exception {
        if (!pathId.equals(sessionUserId)) {
            throw new AccessDeniedException("권한이 없습니다.");
        }
    }
}