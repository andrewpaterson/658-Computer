package net.logicim.domain.common.port;

public class TracePort
    extends Port
{
  public TracePort(String name, PortHolder holder)
  {
    super(PortType.Passive, name, holder);
  }
}

