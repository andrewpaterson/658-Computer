package name.bizna.emu65816.opcode;

import name.bizna.emu65816.Address;
import name.bizna.emu65816.AddressingMode;
import name.bizna.emu65816.Binary;
import name.bizna.emu65816.Cpu65816;

import static name.bizna.emu65816.OpCodeTable.*;

public class OpCode_CMP
    extends OpCode
{
  public OpCode_CMP(String mName, byte mCode, AddressingMode mAddressingMode)
  {
    super(mName, mCode, mAddressingMode);
  }

  protected void execute8BitCMP(Cpu65816 cpu)
  {
    Address valueAddress = cpu.getAddressOfOpCodeData(getAddressingMode());
    byte value = cpu.readByte(valueAddress);
    byte result = (byte) (Binary.lower8BitsOf(cpu.getA()) - value);
    cpu.getCpuStatus().updateSignAndZeroFlagFrom8BitValue(result);
    boolean carry = Binary.lower8BitsOf(cpu.getA()) >= value;
    cpu.getCpuStatus().setCarryFlag(carry);
  }

  protected void execute16BitCMP(Cpu65816 cpu)
  {
    Address valueAddress = cpu.getAddressOfOpCodeData(getAddressingMode());
    short value = cpu.readTwoBytes(valueAddress);
    short result = (short) (cpu.getA() - value);
    cpu.getCpuStatus().updateSignAndZeroFlagFrom16BitValue(result);
    boolean carry = cpu.getA() >= value;
    cpu.getCpuStatus().setCarryFlag(carry);
  }

  private void executeCMP(Cpu65816 cpu)
  {
    if (cpu.accumulatorIs8BitWide())
    {
      execute8BitCMP(cpu);
    }
    else
    {
      execute16BitCMP(cpu);
      cpu.addToCycles(1);
    }
  }

  @Override
  public void execute(Cpu65816 cpu)
  {
    switch (getCode())
    {
      case CMP_Immediate:  // CMP Immediate
      {
        executeCMP(cpu);
        if (cpu.accumulatorIs16BitWide())
        {
          cpu.addToProgramAddress(1);
        }
        cpu.addToProgramAddressAndCycles(2, 2);
        break;
      }
      case CMP_Absolute:  // CMP Absolute
      {
        executeCMP(cpu);
        cpu.addToProgramAddressAndCycles(3, 4);
        break;
      }
      case CMP_AbsoluteLong:  // CMP Absolute Long
      {
        executeCMP(cpu);
        cpu.addToProgramAddressAndCycles(4, 5);
        break;
      }
      case CMP_DirectPage:  // CMP Direct Page
      {
        executeCMP(cpu);
        if (Binary.lower8BitsOf(cpu.getD()) != 0)
        {
          cpu.addToCycles(1);
        }
        cpu.addToProgramAddressAndCycles(2, 3);
        break;
      }
      case CMP_DirectPageIndirect:  // CMP Direct Page Indirect
      {
        executeCMP(cpu);
        if (Binary.lower8BitsOf(cpu.getD()) != 0)
        {
          cpu.addToCycles(1);
        }
        cpu.addToProgramAddressAndCycles(2, 5);
        break;
      }
      case CMP_DirectPageIndirectLong:  // CMP Direct Page Indirect Long
      {
        executeCMP(cpu);
        cpu.addToProgramAddressAndCycles(2, 6);
        break;
      }
      case CMP_AbsoluteIndexedWithX:  // CMP Absolute Indexed, X
      {
        executeCMP(cpu);
        if (cpu.opCodeAddressingCrossesPageBoundary(getAddressingMode()))
        {
          cpu.addToCycles(1);
        }
        cpu.addToProgramAddressAndCycles(3, 4);
        break;
      }
      case CMP_AbsoluteLongIndexedWithX:  // CMP Absolute Long Indexed, X
      {
        executeCMP(cpu);
        cpu.addToProgramAddressAndCycles(4, 5);
        break;
      }
      case CMP_AbsoluteIndexedWithY:  // CMP Absolute Indexed, Y
      {
        executeCMP(cpu);
        if (cpu.opCodeAddressingCrossesPageBoundary(getAddressingMode()))
        {
          cpu.addToCycles(1);
        }
        cpu.addToProgramAddressAndCycles(3, 4);
        break;
      }
      case CMP_DirectPageIndexedWithX:  // CMP Direct Page Indexed, X
      {
        executeCMP(cpu);
        if (Binary.lower8BitsOf(cpu.getD()) != 0)
        {
          cpu.addToCycles(1);
        }
        cpu.addToProgramAddressAndCycles(2, 4);
        break;
      }
      case CMP_DirectPageIndexedIndirectWithX:  // CMP Direct Page Indexed Indirect, X
      {
        executeCMP(cpu);
        if (Binary.lower8BitsOf(cpu.getD()) != 0)
        {
          cpu.addToCycles(1);
        }
        cpu.addToProgramAddressAndCycles(2, 6);
        break;
      }
      case CMP_DirectPageIndirectIndexedWithY:  // CMP Direct Page Indexed Indirect, Y
      {
        executeCMP(cpu);
        if (Binary.lower8BitsOf(cpu.getD()) != 0)
        {
          cpu.addToCycles(1);
        }
        if (cpu.opCodeAddressingCrossesPageBoundary(getAddressingMode()))
        {
          cpu.addToCycles(1);
        }
        cpu.addToProgramAddressAndCycles(2, 5);
        break;
      }
      case CMP_DirectPageIndirectLongIndexedWithY:  // CMP Direct Page Indirect Long Indexed, Y
      {
        executeCMP(cpu);
        if (Binary.lower8BitsOf(cpu.getD()) != 0)
        {
          cpu.addToCycles(1);
        }
        cpu.addToProgramAddressAndCycles(2, 6);
        break;
      }
      case CMP_StackRelative:  // CMP Stack Relative
      {
        executeCMP(cpu);
        cpu.addToProgramAddressAndCycles(2, 4);
        break;
      }
      case CMP_StackRelativeIndirectIndexedWithY:  // CMP Stack Relative Indirect Indexed, Y
      {
        executeCMP(cpu);
        cpu.addToProgramAddressAndCycles(2, 7);
        break;
      }
      default:
        throw new IllegalStateException("Unexpected value: " + getCode());
    }
  }
}

