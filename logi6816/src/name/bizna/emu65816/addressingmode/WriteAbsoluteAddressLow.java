package name.bizna.emu65816.addressingmode;

import name.bizna.emu65816.Binary;
import name.bizna.emu65816.Cpu65816;
import name.bizna.emu65816.opcode.OpCode;

public class WriteAbsoluteAddressLow
    extends DataOperation
{
  public WriteAbsoluteAddressLow()
  {
    super(false, true, true, false, true);
  }

  @Override
  public void execute(Cpu65816 cpu, OpCode opCode)
  {
    cpu.setPinsData(Binary.getLowByte(cpu.getAddress().getOffset()));
  }
}

