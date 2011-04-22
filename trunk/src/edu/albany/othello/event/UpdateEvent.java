package edu.albany.othello.event;

import java.util.EventObject;

@Deprecated
public class UpdateEvent extends EventObject {
    private static final long serialVersionUID = -169147664083234887L;

    public UpdateEvent(Object source) {
        super(source);
    }
}
