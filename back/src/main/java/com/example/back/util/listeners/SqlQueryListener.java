package com.example.back.util.listeners;

import org.eclipse.persistence.sessions.SessionEvent;
import org.eclipse.persistence.sessions.SessionEventAdapter;

public class SqlQueryListener extends SessionEventAdapter {

    private static final ThreadLocal<Boolean> sqlExecuted = ThreadLocal.withInitial(() -> false);

    public static void reset() {
        sqlExecuted.set(false);
    }

    public static boolean wasSqlExecuted() {
        return sqlExecuted.get();
    }

    @Override
    public void preExecuteQuery(SessionEvent event) {
        sqlExecuted.set(true);
    }
}
