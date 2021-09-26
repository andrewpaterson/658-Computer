package name.bizna.emu65816.opcode;

import name.bizna.emu65816.Address;
import name.bizna.emu65816.AddressingMode;
import name.bizna.emu65816.Cpu65816;

import static name.bizna.emu65816.OpCodeTable.*;
import static name.bizna.emu65816.Unsigned.toShort;

public class OpCode_JSR
    extends OpCode
{
  public OpCode_JSR(String mName, int mCode, AddressingMode mAddressingMode)
  {
    super(mName, mCode, mAddressingMode);
  }

  @Override
  public void executeOnFallingEdge(Cpu65816 cpu)
  {
    switch (getCode())
    {
      case JSR_Absolute:  // JSR Absolute
      {
        cpu.push16Bit(toShort(cpu.getProgramAddress().getOffset() + 2));
        int destinationAddress = cpu.getAddressOfOpCodeData(getAddressingMode()).getOffset();
        cpu.setProgramAddress(new Address(cpu.getProgramAddress().getBank(), destinationAddress));
        cpu.addToCycles(6);
        break;
      }
      case JSR_AbsoluteLong:  // JSR Absolute Long
      {
        cpu.push8Bit(cpu.getProgramAddress().getBank());
        cpu.push16Bit(toShort(cpu.getProgramAddress().getOffset() + 3));
        cpu.setProgramAddress(cpu.getAddressOfOpCodeData(getAddressingMode()));
        cpu.addToCycles(8);
        break;
      }
      case JSR_AbsoluteIndexedIndirectWithX:  // JSR Absolute Indexed Indirect, X
      {
        Address destinationAddress = cpu.getAddressOfOpCodeData(getAddressingMode());
        cpu.push8Bit(cpu.getProgramAddress().getBank());
        cpu.push16Bit(toShort(cpu.getProgramAddress().getOffset() + 2));
        cpu.setProgramAddress(destinationAddress);
        cpu.addToCycles(8);
        break;
      }
      default:
        throw new IllegalStateException("Unexpected value: " + getCode());
    }
  }

  @Override
  public void executeOnRisingEdge(Cpu65816 cpu)
  {
  }
}

