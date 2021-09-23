package name.bizna.emu65816.opcode;

import name.bizna.emu65816.Address;
import name.bizna.emu65816.AddressingMode;
import name.bizna.emu65816.Binary;
import name.bizna.emu65816.Cpu65816;

import static name.bizna.emu65816.OpCodeTable.*;

public class OpCode_LDX
    extends OpCode
{
  public OpCode_LDX(String mName, byte mCode, AddressingMode mAddressingMode)
  {
    super(mName, mCode, mAddressingMode);
  }

  protected void executeLDX8Bit(Cpu65816 cpu)
  {
    Address opCodeDataAddress = cpu.getAddressOfOpCodeData(getAddressingMode());
    byte value = cpu.readByte(opCodeDataAddress);
    cpu.setX(Binary.setLower8BitsOf16BitsValue(cpu.getX(), value));
    cpu.getCpuStatus().updateSignAndZeroFlagFrom8BitValue(value);
  }

  protected void executeLDX16Bit(Cpu65816 cpu)
  {
    Address opCodeDataAddress = cpu.getAddressOfOpCodeData(getAddressingMode());
    cpu.setX(cpu.readTwoBytes(opCodeDataAddress));
    cpu.getCpuStatus().updateSignAndZeroFlagFrom16BitValue(cpu.getX());
  }

  @Override
  public void execute(Cpu65816 cpu)
  {
    if (cpu.indexIs16BitWide())
    {
      executeLDX16Bit(cpu);
      cpu.addToCycles(1);
    }
    else
    {
      executeLDX8Bit(cpu);
    }

    switch (getCode())
    {
      case LDX_Immediate:                // LDX Immediate
      {
        if (cpu.indexIs16BitWide())
        {
          cpu.addToProgramAddress(1);
        }
        cpu.addToProgramAddressAndCycles(2, 2);
        break;
      }
      case LDX_Absolute:                // LDX Absolute
      {
        cpu.addToProgramAddressAndCycles(3, 4);
        break;
      }
      case LDX_DirectPage:                // LDX Direct Page
      {
        if (Binary.lower8BitsOf(cpu.getD()) != 0)
        {
          cpu.addToCycles(1);
        }
        cpu.addToProgramAddressAndCycles(2, 3);
        break;
      }
      case LDX_AbsoluteIndexedWithY:                // LDX Absolute Indexed, Y
      {
        if (cpu.opCodeAddressingCrossesPageBoundary(getAddressingMode()))
        {
          cpu.addToCycles(1);
        }
        cpu.addToProgramAddressAndCycles(3, 4);
        break;
      }
      case LDX_DirectPageIndexedWithY:                // LDX Direct Page Indexed, Y
      {
        if (Binary.lower8BitsOf(cpu.getD()) != 0)
        {
          cpu.addToCycles(1);
        }
        cpu.addToProgramAddressAndCycles(2, 4);
        break;
      }
      default:
        throw new IllegalStateException("Unexpected value: " + getCode());
    }
  }
}

