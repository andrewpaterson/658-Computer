package name.bizna.emu65816.opcode;

import name.bizna.emu65816.Address;
import name.bizna.emu65816.AddressingMode;
import name.bizna.emu65816.Binary;
import name.bizna.emu65816.Cpu65816;

import static name.bizna.emu65816.OpCodeTable.STZ_Absolute;
import static name.bizna.emu65816.OpCodeTable.STZ_AbsoluteIndexedWithX;

public class OpCode_STZ
    extends OpCode
{
  public OpCode_STZ(String mName, byte mCode, AddressingMode mAddressingMode)
  {
    super(mName, mCode, mAddressingMode);
  }

  @Override
  public void execute(Cpu65816 cpu)
  {
    Address dataAddress = cpu.getAddressOfOpCodeData(getAddressingMode());
    if (cpu.accumulatorIs8BitWide())
    {
      cpu.storeByte(dataAddress, 0x00);
    }
    else
    {
      cpu.storeTwoBytes(dataAddress, 0x0000);
      cpu.addToCycles(1);
    }

    switch (getCode())
    {
      case STZ_Absolute:  // STZ Absolute
      {
        cpu.addToProgramAddress(3);
        cpu.addToCycles(4);
        break;
      }
      case (0x64):  // STZ Direct Page
      {
        if (Binary.lower8BitsOf(cpu.getD()) != 0)
        {
          cpu.addToCycles(1);
        }

        cpu.addToProgramAddress(2);
        cpu.addToCycles(3);
        break;
      }
      case STZ_AbsoluteIndexedWithX:  // STZ Absolute Indexed, X
      {
        cpu.addToProgramAddress(3);
        cpu.addToCycles(5);
        break;
      }
      case (0x74):  // STZ Direct Page Indexed, X
      {
        if (Binary.lower8BitsOf(cpu.getD()) != 0)
        {
          cpu.addToCycles(1);
        }

        cpu.addToProgramAddress(2);
        cpu.addToCycles(4);
        break;
      }
      default:
        throw new IllegalStateException("Unexpected value: " + getCode());
    }
  }
}

