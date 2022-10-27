package net.logicim.ui.clock;

import net.logicim.domain.common.Circuit;
import net.logicim.domain.integratedcircuit.standard.clock.ClockOscillator;
import net.logicim.domain.integratedcircuit.standard.clock.ClockOscillatorPins;
import net.logicim.ui.common.View;

import static net.logicim.domain.common.Units.Hz;

public class ClockView
    extends View
{
  protected ClockOscillator clock;

  public ClockView(Circuit circuit)
  {
    this.clock = new ClockOscillator(circuit, "", new ClockOscillatorPins(), 1 * Hz);
  }

  public void paint()
  {
  }
}

