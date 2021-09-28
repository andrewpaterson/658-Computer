package name.bizna.emu65816.opcode;

import name.bizna.emu65816.Address;
import name.bizna.emu65816.AddressingMode;
import name.bizna.emu65816.Binary;
import name.bizna.emu65816.Cpu65816;

import static name.bizna.emu65816.OpCodeName.*;
import static name.bizna.emu65816.Unsigned.toByte;
import static name.bizna.emu65816.Unsigned.toShort;

public class OpCode_INC
    extends OpCode
{
  public OpCode_INC(String mName, int mCode, AddressingMode mAddressingMode)
  {
    super(mName, mCode, mAddressingMode);
  }

  protected void execute8BitIncInMemory(Cpu65816 cpu)
  {
    Address opCodeDataAddress = cpu.getAddressOfOpCodeData(getAddressingMode());
    int value = cpu.get8BitData();
    value++;
    value = toByte(value);
    cpu.getCpuStatus().updateSignAndZeroFlagFrom8BitValue(value);
    cpu.storeByte(opCodeDataAddress, value);
  }

  protected void execute16BitIncInMemory(Cpu65816 cpu)
  {
    Address opCodeDataAddress = cpu.getAddressOfOpCodeData(getAddressingMode());
    int value = cpu.get16BitData();
    value++;
    value = toShort(value);
    cpu.getCpuStatus().updateSignAndZeroFlagFrom16BitValue(value);
    cpu.storeTwoBytes(opCodeDataAddress, value);
  }

  @Override
  public void executeOnFallingEdge(Cpu65816 cpu)
  {
    switch (getCode())
    {
      case INC_Accumulator:  // INC Accumulator
      {
        if (cpu.isAccumulator8Bit())
        {
          int lowerA = Binary.lower8BitsOf(cpu.getA());
          lowerA++;
          lowerA = toByte(lowerA);
          cpu.setA(Binary.setLower8BitsOf16BitsValue(cpu.getA(), lowerA));
          cpu.getCpuStatus().updateSignAndZeroFlagFrom8BitValue(lowerA);
        }
        else
        {
          int a = (cpu.getA());
          a++;
          a = toShort(a);
          cpu.setA(a);
          cpu.getCpuStatus().updateSignAndZeroFlagFrom16BitValue(a);
        }
        cpu.addToProgramAddressAndCycles(1, 2);
        break;
      }
      case INC_Absolute: // INC Absolute
      {
        if (cpu.isAccumulator8Bit())
        {
          execute8BitIncInMemory(cpu);
        }
        else
        {
          execute16BitIncInMemory(cpu);
          cpu.addToCycles(2);
        }
        cpu.addToProgramAddressAndCycles(3, 6);
        break;
      }
      case INC_DirectPage: // INC Direct Page
      {
        if (cpu.isAccumulator8Bit())
        {
          execute8BitIncInMemory(cpu);
        }
        else
        {
          execute16BitIncInMemory(cpu);
          cpu.addToCycles(2);
        }
        if (Binary.lower8BitsOf(cpu.getDirectPage()) != 0)
        {
          cpu.addToCycles(1);
        }
        cpu.addToProgramAddressAndCycles(2, 5);
        break;
      }
      case INC_AbsoluteIndexedWithX: // INC Absolute Indexed, X
      {
        if (cpu.isAccumulator8Bit())
        {
          execute8BitIncInMemory(cpu);
        }
        else
        {
          execute16BitIncInMemory(cpu);
          cpu.addToCycles(2);
        }

        cpu.addToProgramAddressAndCycles(3, 7);
      }
      break;
      case INC_DirectPageIndexedWithX: // INC Direct Page Indexed, X
      {
        if (cpu.isAccumulator8Bit())
        {
          execute8BitIncInMemory(cpu);
        }
        else
        {
          execute16BitIncInMemory(cpu);
          cpu.addToCycles(2);
        }
        if (Binary.lower8BitsOf(cpu.getDirectPage()) != 0)
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

  @Override
  public void executeOnRisingEdge(Cpu65816 cpu)
  {
  }
}

