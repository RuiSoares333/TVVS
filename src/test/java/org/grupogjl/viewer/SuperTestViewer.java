package org.grupogjl.viewer;

import org.grupogjl.gui.GeneralGui;
import org.junit.jupiter.api.BeforeEach;

public abstract class SuperTestViewer {

    protected Viewer viewer;
    protected GeneralGui gui;

    @BeforeEach
    void setUp() {
        initMocks();
        initReturns();
    }

    abstract void initMocks();
    abstract void initReturns();
}
