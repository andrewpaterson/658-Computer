package name.bizna.emu65816.opcode;

import name.bizna.emu65816.Address;
import name.bizna.emu65816.Binary;
import name.bizna.emu65816.Cpu65816;
import name.bizna.emu65816.addressingmode.InstructionCycles;

import static name.bizna.emu65816.OpCodeName.*;
import static name.bizna.emu65816.Unsigned.toByte;
import static name.bizna.emu65816.Unsigned.toShort;

public class OpCode_TRB
    extends OpCode
{
  public OpCode_TRB(int mCode, InstructionCycles cycles)
  {
    super("TRB", "Test and Reset Bit", mCode, cycles);
  }

  protected void execute8BitTRB(Cpu65816 cpu)
  {
    Address addressOfOpCodeData = cpu.getAddressOfOpCodeData(getAddressingMode());
    int value = cpu.getDataLow();
    int lowerA = Binary.getLowByte(cpu.getA());
    int result = toByte(value & ~lowerA);
    cpu.storeByte(addressOfOpCodeData, result);
    cpu.getCpuStatus().setZeroFlag((value & lowerA) == 0);
  }

  protected void execute16BitTRB(Cpu65816 cpu)
  {
    Address addressOfOpCodeData = cpu.getAddressOfOpCodeData(getAddressingMode());
    int value = cpu.getData();
    int result = toShort(value & ~cpu.getA());
    cpu.storeTwoBytes(addressOfOpCodeData, result);
    cpu.getCpuStatus().setZeroFlag((value & cpu.getA()) == 0);
  }

  @Override
  public void executeOnFallingEdge(Cpu65816 cpu)
  {
    switch (getCode())
    {
      case TRB_Absolute:                 // TRB Absolute
      {
        if (cpu.isMemory8Bit())
        {
          execute8BitTRB(cpu);
        }
        else
        {
          execute16BitTRB(cpu);
          cpu.addToCycles(2);
        }
        cpu.addToProgramAddressAndCycles(3, 6);
        break;
      }
      case TRB_DirectPage:                 // TRB Direct Page
      {
        if (cpu.isMemory8Bit())
        {
          execute8BitTRB(cpu);
        }
        else
        {
          execute16BitTRB(cpu);
          cpu.addToCycles(2);
        }
        if (Binary.getLowByte(cpu.getDirectPage()) != 0)
        {
          cpu.addToCycles(1);
        }
        cpu.addToProgramAddressAndCycles(2, 5);
        break;
      }
      default:
        throw new IllegalStateException("Unexpected value: " + getCode());
    }
  }
}

