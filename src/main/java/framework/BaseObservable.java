/**
 * Package location for framework concepts.
 */
package framework;

import java.util.Observable;

/**
 * Represents a base observable.
 *
 * @author Ivo Ferro
 */
public class BaseObservable extends Observable {

    public void activateChanges() {
        setChanged();
    }

    public void deactivateChanges() {
        clearChanged();
    }

}
