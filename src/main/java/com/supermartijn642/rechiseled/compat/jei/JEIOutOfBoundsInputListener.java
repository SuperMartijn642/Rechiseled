package com.supermartijn642.rechiseled.compat.jei;

import mezz.jei.api.gui.inputs.IJeiUserInput;

/**
 * Created 12/02/2026 by SuperMartijn642
 */
public interface JEIOutOfBoundsInputListener {

    void rechiseledOutOfBoundsInput(double mouseX, double mouseY, IJeiUserInput input);
}
