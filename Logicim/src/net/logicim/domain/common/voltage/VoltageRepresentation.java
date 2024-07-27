package net.logicim.domain.common.voltage;

import java.awt.*;

public interface VoltageRepresentation
{
  Color getDisconnectedTrace();

  Color getTraceError();

  Color getTraceVoltage(float voltage);

  Color getTraceShort(float shortVoltage);

  Color getTraceUndriven();
}
