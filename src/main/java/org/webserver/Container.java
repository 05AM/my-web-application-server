package org.webserver;

import org.webserver.util.logging.Log;
import org.webserver.util.logging.Logger;

/**
 * 요청을 받아 다음 단계로 전달하거나 처리하는 컴포넌트
 */
public interface Container {

    Container getContainer();

    void setContainer(Container container);

    Pipeline getPipeline();

    default Log getLogger() {
        return Logger.getInstance(this.getClass());
    }
}

