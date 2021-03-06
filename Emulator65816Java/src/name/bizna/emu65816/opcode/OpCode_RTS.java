package name.bizna.emu65816.opcode;

import name.bizna.emu65816.Address;
import name.bizna.emu65816.AddressingMode;
import name.bizna.emu65816.Cpu65816;

import static name.bizna.emu65816.Unsigned.toShort;

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
    Address returnAddress = new Address(cpu.getProgramAddress().getBank(), toShort (cpu.getStack().pull16Bit() + 1));
    cpu.setProgramAddress(returnAddress);
    cpu.addToCycles(6);
  }
}

