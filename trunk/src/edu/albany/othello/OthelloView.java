package edu.albany.othello;

import edu.albany.othello.event.UpdateEvent;
import edu.albany.othello.event.UpdateListener;

public class OthelloView implements UpdateListener {
    @Override
    public void updatePerformed(UpdateEvent e) {
        if (!(e.getSource() instanceof OthelloModel)) {
            // TODO: Should we throw here? Not very important.
            return;
        }
    }
}
