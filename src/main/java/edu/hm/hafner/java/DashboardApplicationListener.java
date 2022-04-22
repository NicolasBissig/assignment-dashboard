package edu.hm.hafner.java;

import org.springframework.boot.ExitCodeEvent;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;

/**
 * FIXME: comment class.
 *
 * @author Ullrich Hafner
 */
public class DashboardApplicationListener implements ApplicationListener<ExitCodeEvent> {
    @Override
    public void onApplicationEvent(final ExitCodeEvent event) {
        System.out.println();
    }
}
