package net.logicim.domain.common.helper;

import net.logicim.domain.common.IntegratedCircuit;
import net.logicim.domain.common.wire.Trace;
import net.logicim.domain.passive.power.PowerSource;
import net.logicim.domain.passive.subcircuit.SubcircuitSimulation;

public class TestPower
{
  private float vcc;
  private float gnd;
  private Trace vccTrace;
  private Trace gndTrace;

  public TestPower(SubcircuitSimulation subcircuitSimulation, float vcc)
  {
    this.vcc = vcc;
    this.gnd = 0.0f;

    vccTrace = new Trace();
    gndTrace = new Trace();
    PowerSource vccPowerSource = new PowerSource(subcircuitSimulation, "VCC", vcc);
    PowerSource gndPowerSource = new PowerSource(subcircuitSimulation, "GND", gnd);
    vccPowerSource.getPowerOutPort().connect(vccTrace, false);
    gndPowerSource.getPowerOutPort().connect(gndTrace, false);
  }

  public void connect(IntegratedCircuit integratedCircuit)
  {
    integratedCircuit.getPins().getVoltageGround().connect(gndTrace);
    integratedCircuit.getPins().getVoltageCommon().connect(vccTrace);
  }

  public float getVCC()
  {
    return vcc;
  }

  public float getGND()
  {
    return gnd;
  }
}

