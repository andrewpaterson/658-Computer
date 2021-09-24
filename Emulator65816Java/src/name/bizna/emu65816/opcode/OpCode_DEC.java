package name.bizna.emu65816.opcode;

import name.bizna.emu65816.Address;
import name.bizna.emu65816.AddressingMode;
import name.bizna.emu65816.Binary;
import name.bizna.emu65816.Cpu65816;

import static name.bizna.emu65816.OpCodeTable.*;
import static name.bizna.emu65816.OpCodeTable.DEC_DirectPageIndexedWithX;
import static name.bizna.emu65816.Unsigned.toByte;
import static name.bizna.emu65816.Unsigned.toShort;

public class OpCode_DEC
    extends OpCode
{
  public OpCode_DEC(String mName, byte mCode, AddressingMode mAddressingMode)
  {
    super(mName, mCode, mAddressingMode);
  }

  protected void execute8BitDecInMemory(Cpu65816 cpu)
  {
    Address opCodeDataAddress = cpu.getAddressOfOpCodeData(getAddressingMode());
    int value = cpu.readByte(opCodeDataAddress);
    value--;
    value = toByte(value);
    cpu.getCpuStatus().updateSignAndZeroFlagFrom8BitValue(value);
    cpu.storeByte(opCodeDataAddress, value);
  }

  protected void execute16BitDecInMemory(Cpu65816 cpu)
  {
    Address opCodeDataAddress = cpu.getAddressOfOpCodeData(getAddressingMode());
    int value = cpu.readTwoBytes(opCodeDataAddress);
    value--;
    value = toShort(value);
    cpu.getCpuStatus().updateSignAndZeroFlagFrom16BitValue(value);
    cpu.storeTwoBytes(opCodeDataAddress, value);
  }

  @Override
  public void execute(Cpu65816 cpu)
  {
    switch (getCode())
    {
      case DEC_Accumulator:  // DEC Accumulator
      {
        if (cpu.accumulatorIs8BitWide())
        {
          int lowerA = Binary.lower8BitsOf(cpu.getA());
          lowerA--;
          lowerA = toByte(lowerA);
          cpu.setA(Binary.setLower8BitsOf16BitsValue(cpu.getA(), lowerA));
          cpu.getCpuStatus().updateSignAndZeroFlagFrom8BitValue(lowerA);
        }
        else
        {
          cpu.decA();
          cpu.getCpuStatus().updateSignAndZeroFlagFrom16BitValue(cpu.getA());
        }
        cpu.addToProgramAddressAndCycles(1, 2);
        break;
      }
      case DEC_Absolute: // DEC Absolute
      {
        if (cpu.accumulatorIs8BitWide())
        {
          execute8BitDecInMemory(cpu);
        }
        else
        {
          execute16BitDecInMemory(cpu);
          cpu.addToCycles(2);
        }
        cpu.addToProgramAddressAndCycles(3, 6);
        break;
      }
      case DEC_DirectPage: // DEC Direct Page
      {
        if (cpu.accumulatorIs8BitWide())
        {
          execute8BitDecInMemory(cpu);
        }
        else
        {
          execute16BitDecInMemory(cpu);
          cpu.addToCycles(2);
        }
        if (Binary.lower8BitsOf(cpu.getD()) != 0)
        {
          cpu.addToCycles(1);
        }
        cpu.addToProgramAddressAndCycles(2, 5);
        break;
      }
      case DEC_AbsoluteIndexedWithX: // DEC Absolute Indexed, X
      {
        if (cpu.accumulatorIs8BitWide())
        {
          execute8BitDecInMemory(cpu);
        }
        else
        {
          execute16BitDecInMemory(cpu);
          cpu.addToCycles(2);
        }

        cpu.addToProgramAddressAndCycles(3, 7);
        break;
      }
      case DEC_DirectPageIndexedWithX: // DEC Direct Page Indexed, X
      {
        if (cpu.accumulatorIs8BitWide())
        {
          execute8BitDecInMemory(cpu);
        }
        else
        {
          execute16BitDecInMemory(cpu);
          cpu.addToCycles(2);
        }
        if (Binary.lower8BitsOf(cpu.getD()) != 0)
        {
          cpu.addToCycles(1);
        }
        cpu.addToProgramAddressAndCycles(2, 6);
        break;
      }
      default:
        throw new IllegalStateException("Unexpected value: " + getCode());
    }
  }
}

