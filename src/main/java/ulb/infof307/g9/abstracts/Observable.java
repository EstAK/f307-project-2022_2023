package ulb.infof307.g9.abstracts;

/**
 * The type Observable.
 *
 * @param <IListener> the interface of the listener that will be notified
 */
public interface Observable<IListener> {
    void setListener(IListener listener);
}
