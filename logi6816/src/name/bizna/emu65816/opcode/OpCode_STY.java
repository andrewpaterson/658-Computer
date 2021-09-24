package name.bizna.emu65816.opcode;

import name.bizna.emu65816.Address;
import name.bizna.emu65816.AddressingMode;
import name.bizna.emu65816.Binary;
import name.bizna.emu65816.Cpu65816;

import static name.bizna.emu65816.OpCodeTable.*;

public class OpCode_STY
    extends OpCode
{
  public OpCode_STY(String mName, int mCode, AddressingMode mAddressingMode)
  {
    super(mName, mCode, mAddressingMode);
  }

  @Override
  public void execute(Cpu65816 cpu, int cycle, boolean clock)
  {
    Address dataAddress = cpu.getAddressOfOpCodeData(getAddressingMode());
    if (cpu.accumulatorIs8BitWide())
    {
      cpu.storeByte(dataAddress, Binary.lower8BitsOf(cpu.getY()));
    }
    else
    {
      cpu.storeTwoBytes(dataAddress, cpu.getY());
      cpu.addToCycles(1);
    }

    switch (getCode())
    {
      case STY_Absolute:  // STY Absolute
      {
        cpu.addToProgramAddress(3);
        cpu.addToCycles(4);
        break;
      }
      case STY_DirectPage:  // STY Direct Page
      {
        if (Binary.lower8BitsOf(cpu.getD()) != 0)
        {
          cpu.addToCycles(1);
        }

        cpu.addToProgramAddress(2);
        cpu.addToCycles(3);
        break;
      }
      case STY_DirectPageIndexedWithX:  // STY Direct Page Indexed, X
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

