package name.bizna.emu65816.opcode;

import name.bizna.emu65816.Address;
import name.bizna.emu65816.AddressingMode;
import name.bizna.emu65816.Binary;
import name.bizna.emu65816.Cpu65816;

import static name.bizna.emu65816.OpCodeTable.*;

public class OpCode_STX
    extends OpCode
{
  public OpCode_STX(String mName, byte mCode, AddressingMode mAddressingMode)
  {
    super(mName, mCode, mAddressingMode);
  }

  @Override
  public void execute(Cpu65816 cpu)
  {
    Address dataAddress = cpu.getAddressOfOpCodeData(getAddressingMode());
    if (cpu.accumulatorIs8BitWide())
    {
      cpu.storeByte(dataAddress, Binary.lower8BitsOf(cpu.getX()));
    }
    else
    {
      cpu.storeTwoBytes(dataAddress, cpu.getX());
      cpu.addToCycles(1);
    }

    switch (getCode())
    {
      case STX_Absolute:  // STX Absolute
      {
        cpu.addToProgramAddress(3);
        cpu.addToCycles(4);
        break;
      }
      case STX_DirectPage:  // STX Direct Page
      {
        if (Binary.lower8BitsOf(cpu.getD()) != 0)
        {
          cpu.addToCycles(1);
        }

        cpu.addToProgramAddress(2);
        cpu.addToCycles(3);
        break;
      }
      case STX_DirectPageIndexedWithY:  // STX Direct Page Indexed, Y
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

