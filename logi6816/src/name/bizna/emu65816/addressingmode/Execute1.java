package name.bizna.emu65816.addressingmode;

import name.bizna.emu65816.Cpu65816;
import name.bizna.emu65816.opcode.OpCode;

public class Execute
    extends Operation
{
  private final int executeCycle;

  public Execute(int executeCycle)
  {
    this.executeCycle = executeCycle;
  }

  @Override
  public void execute(Cpu65816 cpu, OpCode opCode)
  {
    opCode.execute(cpu, executeCycle);
  }
}

