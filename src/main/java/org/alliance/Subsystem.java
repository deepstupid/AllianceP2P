package org.alliance;


/**
 * Created by IntelliJ IDEA.
 * User: maciek
 * Date: 2005-dec-30
 * Time: 16:47:11
 */
public interface Subsystem {

    public static interface ResourceLoader {

    }

    void init(ResourceLoader rl, Object... params) throws Exception;

    void shutdown();
}
