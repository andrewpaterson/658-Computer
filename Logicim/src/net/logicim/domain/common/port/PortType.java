package net.logicim.domain.common.port;

public enum PortType
{
  Input,
  Output,
  Bidirectional,  //implies that another pin is explicitly selecting if the port is treated as an Input or an Output.
  PowerIn,
  PowerOut,
  Passive,
  NotConnected,
}

