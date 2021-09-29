package name.bizna.emu65816.opcode;

import name.bizna.emu65816.Address;
import name.bizna.emu65816.AddressingMode;
import name.bizna.emu65816.Binary;
import name.bizna.emu65816.Cpu65816;

import static name.bizna.emu65816.OpCodeName.*;

public class OpCode_LDX
    extends OpCode
{
  public OpCode_LDX(String mName, int mCode, AddressingMode mAddressingMode)
  {
    super(mName, mCode, mAddressingMode);
  }

  protected void executeLDX8Bit(Cpu65816 cpu)
  {
    Address opCodeDataAddress = cpu.getAddressOfOpCodeData(getAddressingMode());
    int value = cpu.get8BitData();
    cpu.setX(Binary.setLower8BitsOf16BitsValue(cpu.getX(), value));
    cpu.getCpuStatus().updateSignAndZeroFlagFrom8BitValue(value);
  }

  protected void executeLDX16Bit(Cpu65816 cpu)
  {
    Address opCodeDataAddress = cpu.getAddressOfOpCodeData(getAddressingMode());
    cpu.setX(cpu.get16BitData());
    cpu.getCpuStatus().updateSignAndZeroFlagFrom16BitValue(cpu.getX());
  }

  @Override
  public void executeOnFallingEdge(Cpu65816 cpu)
  {
    if (cpu.isIndex16Bit())
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
        if (cpu.isIndex16Bit())
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
        if (Binary.getLowByte(cpu.getDirectPage()) != 0)
        {
          cpu.addToCycles(1);
        }
        cpu.addToProgramAddressAndCycles(2, 3);
        break;
      }
      case LDX_AbsoluteIndexedWithY:                // LDX Absolute Indexed, Y
      {
        cpu.addToProgramAddressAndCycles(3, 4);
        break;
      }
      case LDX_DirectPageIndexedWithY:                // LDX Direct Page Indexed, Y
      {
        if (Binary.getLowByte(cpu.getDirectPage()) != 0)
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

  @Override
  public void executeOnRisingEdge(Cpu65816 cpu)
  {
  }
}

