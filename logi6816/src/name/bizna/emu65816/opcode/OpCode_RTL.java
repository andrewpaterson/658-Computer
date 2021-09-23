package name.bizna.emu65816.opcode;

import name.bizna.emu65816.Address;
import name.bizna.emu65816.AddressingMode;
import name.bizna.emu65816.Cpu65816;

public class OpCode_RTL
    extends OpCode
{
  public OpCode_RTL(String mName, byte mCode, AddressingMode mAddressingMode)
  {
    super(mName, mCode, mAddressingMode);
  }

  @Override
  public void execute(Cpu65816 cpu)
  {
    short newOffset = (short) (cpu.getStack().pull16Bit(cpu) + 1);
    byte newBank = cpu.getStack().pull8Bit(cpu);

    Address returnAddress = new Address(newBank, newOffset);
    cpu.setProgramAddress(returnAddress);
    cpu.addToCycles(6);
  }
}

