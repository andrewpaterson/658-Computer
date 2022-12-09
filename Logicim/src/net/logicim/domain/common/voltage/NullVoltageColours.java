package net.logicim.domain.common.voltage;

import java.awt.*;

public class NullVoltageColours
    implements VoltageRepresentation
{
  protected Color black;

  public NullVoltageColours()
  {
    black = Color.BLACK;
  }

  @Override
  public Color getDisconnectedTrace()
  {
    return black;
  }

  @Override
  public Color getTraceError()
  {
    return black;
  }

  @Override
  public Color getTraceVoltage(float voltage)
  {
    return black;
  }

  @Override
  public Color getTraceShort(float shortVoltage)
  {
    return black;
  }

  @Override
  public Color getTraceUndriven()
  {
    return black;
  }
}

