package org.example;

import java.util.EventListener;

public interface DataSheetChangeListener extends EventListener {

    void dataChanged(DataSheetChangeEvent e);
}
