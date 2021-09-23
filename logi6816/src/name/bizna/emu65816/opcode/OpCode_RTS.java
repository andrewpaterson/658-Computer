package name.bizna.emu65816.opcode;

import name.bizna.emu65816.Address;
import name.bizna.emu65816.AddressingMode;
import name.bizna.emu65816.Cpu65816;

public class OpCode_RTS
    extends OpCode
{
  public OpCode_RTS(String mName, byte mCode, AddressingMode mAddressingMode)
  {
    super(mName, mCode, mAddressingMode);
  }

  @Override
  public void execute(Cpu65816 cpu)
  {
    Address returnAddress = new Address(cpu.getProgramAddress().getBank(), (short) (cpu.getStack().pull16Bit(cpu) + 1));
    cpu.setProgramAddress(returnAddress);
    cpu.addToCycles(6);
  }
}

