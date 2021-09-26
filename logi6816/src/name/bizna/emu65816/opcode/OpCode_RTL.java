package name.bizna.emu65816.opcode;

import name.bizna.emu65816.Address;
import name.bizna.emu65816.AddressingMode;
import name.bizna.emu65816.Cpu65816;

import static name.bizna.emu65816.Unsigned.toShort;

public class OpCode_RTL
    extends OpCode
{
  public OpCode_RTL(String mName, int mCode, AddressingMode mAddressingMode)
  {
    super(mName, mCode, mAddressingMode);
  }

  @Override
  public void executeOnFallingEdge(Cpu65816 cpu)
  {
    int newOffset = toShort(cpu.pull16Bit() + 1);
    int newBank = cpu.pull8Bit();

    Address returnAddress = new Address(newBank, newOffset);
    cpu.setProgramAddress(returnAddress);
    cpu.addToCycles(6);
  }

  @Override
  public void executeOnRisingEdge(Cpu65816 cpu)
  {
  }
}

