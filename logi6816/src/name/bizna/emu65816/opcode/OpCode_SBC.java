package name.bizna.emu65816.opcode;

import name.bizna.emu65816.*;

import static name.bizna.emu65816.Binary.is8bitValueNegative;
import static name.bizna.emu65816.OpCodeName.*;
import static name.bizna.emu65816.Unsigned.toByte;
import static name.bizna.emu65816.Unsigned.toShort;

public class OpCode_SBC
    extends OpCode
{
  public OpCode_SBC(String mName, int mCode, AddressingMode mAddressingMode)
  {
    super(mName, mCode, mAddressingMode);
  }

  public void execute8BitSBC(Cpu65816 cpu)
  {
    Address dataAddress = cpu.getAddressOfOpCodeData(getAddressingMode());
    int value = cpu.get8BitData();
    int accumulator = Binary.getLowByte(cpu.getA());
    boolean borrow = !cpu.getCpuStatus().carryFlag();

    int result16Bit = toShort(accumulator - value - (borrow ? 1 : 0));

    // Is there a borrow from the penultimate bit, redo the diff with 7 bits value and find out.
    accumulator &= 0x7F;
    value &= 0x7F;
    int partialResult = toByte(accumulator - value - (borrow ? 1 : 0));
    // Is bit 8 set?
    boolean borrowFromPenultimateBit = is8bitValueNegative(partialResult);

    // Is there a borrow from the last bit, check bit 8 for that
    boolean borrowFromLastBit = (result16Bit & 0x0100) != 0;

    boolean overflow = borrowFromLastBit ^ borrowFromPenultimateBit;
    cpu.getCpuStatus().setOverflowFlag(overflow);

    cpu.getCpuStatus().setCarryFlag(!borrowFromLastBit);

    int result8Bit = Binary.getLowByte(result16Bit);
    // Update sign and zero flags
    cpu.getCpuStatus().updateSignAndZeroFlagFrom8BitValue(result8Bit);
    // Store the 8 bit result in the accumulator
    cpu.setA(Binary.setLower8BitsOf16BitsValue(cpu.getA(), result8Bit));
  }

  protected void execute16BitSBC(Cpu65816 cpu)
  {
    Address dataAddress = cpu.getAddressOfOpCodeData(getAddressingMode());
    int value = cpu.get16BitData();
    int accumulator = cpu.getA();
    boolean borrow = !cpu.getCpuStatus().carryFlag();

    int result32Bit = accumulator - value - (borrow ? 1 : 0);

    // Is there a borrow from the penultimate bit, redo the diff with 15 bits value and find out.
    accumulator &= 0x7FFF;
    value &= 0x7FFF;
    int partialResult = toShort(accumulator - value - (borrow ? 1 : 0));
    // Is bit 15 set?
    boolean borrowFromPenultimateBit = is8bitValueNegative(partialResult);

    // Is there a borrow from the last bit, check bit 16 for that
    boolean borrowFromLastBit = (result32Bit & 0x0100) != 0;

    boolean overflow = borrowFromLastBit ^ borrowFromPenultimateBit;
    cpu.getCpuStatus().setOverflowFlag(overflow);

    cpu.getCpuStatus().setCarryFlag(!borrowFromLastBit);

    int result16Bit = Binary.getLowByte(result32Bit);
    // Update sign and zero flags
    cpu.getCpuStatus().updateSignAndZeroFlagFrom8BitValue(toByte(result16Bit));
    // Store the 8 bit result in the accumulator
    cpu.setA(result16Bit);
  }

  protected void execute8BitBCDSBC(Cpu65816 cpu)
  {
    Address dataAddress = cpu.getAddressOfOpCodeData(getAddressingMode());
    int value = cpu.get8BitData();
    int accumulator = Binary.getLowByte(cpu.getA());

    BCDResult bcd8BitResult = Binary.bcdSubtract8Bit(value, accumulator, !cpu.getCpuStatus().carryFlag());
    boolean borrow = bcd8BitResult.carry;
    int result = bcd8BitResult.value;
    cpu.getCpuStatus().setCarryFlag(!borrow);

    cpu.setA(Binary.setLower8BitsOf16BitsValue(cpu.getA(), result));
    cpu.getCpuStatus().updateSignAndZeroFlagFrom8BitValue(result);
  }

  protected void execute16BitBCDSBC(Cpu65816 cpu)
  {
    Address dataAddress = cpu.getAddressOfOpCodeData(getAddressingMode());
    int value = cpu.get16BitData();
    int accumulator = cpu.getA();

    BCDResult bcd16BitResult = Binary.bcdSubtract16Bit(value, accumulator, !cpu.getCpuStatus().carryFlag());
    boolean borrow = bcd16BitResult.carry;
    int result = bcd16BitResult.value;
    cpu.getCpuStatus().setCarryFlag(!borrow);

    cpu.setA(result);
    cpu.getCpuStatus().updateSignAndZeroFlagFrom8BitValue(toByte(result));
  }

  @Override
  public void executeOnFallingEdge(Cpu65816 cpu)
  {
    if (cpu.isMemory8Bit())
    {
      if (cpu.getCpuStatus().decimalFlag())
      {
        execute8BitBCDSBC(cpu);
      }
      else
      {
        execute8BitSBC(cpu);
      }
    }
    else
    {
      if (cpu.getCpuStatus().decimalFlag())
      {
        execute16BitBCDSBC(cpu);
      }
      else
      {
        execute16BitSBC(cpu);
      }
      cpu.addToCycles(1);
    }

    switch (getCode())
    {
      case SBC_Immediate:                 // SBC Immediate
      {
        if (cpu.isMemory16Bit())
        {
          cpu.addToProgramAddress(1);
        }
        cpu.addToProgramAddress(2);
        cpu.addToCycles(2);
        break;
      }
      case SBC_Absolute:                 // SBC Absolute
      {
        cpu.addToProgramAddress(3);
        cpu.addToCycles(4);
        break;
      }
      case SBC_AbsoluteLong:                 // SBC Absolute Long
      {
        cpu.addToProgramAddress(4);
        cpu.addToCycles(5);
        break;
      }
      case SBC_DirectPage:                 // SBC Direct Page
      {
        if (Binary.getLowByte(cpu.getDirectPage()) != 0)
        {
          cpu.addToCycles(1);
        }

        cpu.addToProgramAddress(2);
        cpu.addToCycles(3);
        break;
      }
      case SBC_DirectPageIndirect:                 // SBC Direct Page Indirect
      {
        if (Binary.getLowByte(cpu.getDirectPage()) != 0)
        {
          cpu.addToCycles(1);
        }

        cpu.addToProgramAddress(2);
        cpu.addToCycles(5);
        break;
      }
      case SBC_DirectPageIndirectLong:                 // SBC Direct Page Indirect Long
      {
        if (Binary.getLowByte(cpu.getDirectPage()) != 0)
        {
          cpu.addToCycles(1);
        }

        cpu.addToProgramAddress(2);
        cpu.addToCycles(6);
        break;
      }
      case SBC_AbsoluteIndexedWithX:                 // SBC Absolute Indexed, X
      {
        cpu.addToProgramAddress(3);
        cpu.addToCycles(4);
        break;
      }
      case SBC_AbsoluteLongIndexedWithX:                 // SBC Absolute Long Indexed, X
      {
        cpu.addToProgramAddress(4);
        cpu.addToCycles(5);
        break;
      }
      case SBC_AbsoluteIndexedWithY:                 // SBC Absolute Indexed Y
      {
        cpu.addToProgramAddress(3);
        cpu.addToCycles(4);
        break;
      }
      case SBC_DirectPageIndexedWithX:                 // SBC Direct Page Indexed, X
      {
        if (Binary.getLowByte(cpu.getDirectPage()) != 0)
        {
          cpu.addToCycles(1);
        }
        cpu.addToProgramAddress(2);
        cpu.addToCycles(4);
        break;
      }
      case SBC_DirectPageIndexedIndirectWithX:                 // SBC Direct Page Indexed Indirect, X
      {
        if (Binary.getLowByte(cpu.getDirectPage()) != 0)
        {
          cpu.addToCycles(1);
        }
        cpu.addToProgramAddress(2);
        cpu.addToCycles(6);
        break;
      }
      case SBC_DirectPageIndirectIndexedWithY:                 // SBC Direct Page Indirect Indexed, Y
      {
        if (Binary.getLowByte(cpu.getDirectPage()) != 0)
        {
          cpu.addToCycles(1);
        }
        cpu.addToProgramAddress(2);
        cpu.addToCycles(5);
        break;
      }
      case SBC_DirectPageIndirectLongIndexedWithY:                 // SBC Direct Page Indirect Long Indexed, Y
      {
        if (Binary.getLowByte(cpu.getDirectPage()) != 0)
        {
          cpu.addToCycles(1);
        }
        cpu.addToProgramAddress(2);
        cpu.addToCycles(6);
        break;
      }
      case SBC_StackRelative:                 // SBC Stack Relative
      {
        cpu.addToProgramAddress(2);
        cpu.addToCycles(4);
        break;
      }
      case SBC_StackRelativeIndirectIndexedWithY:                 // SBC Stack Relative Indirect Indexed, Y
      {
        cpu.addToProgramAddress(2);
        cpu.addToCycles(7);
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

