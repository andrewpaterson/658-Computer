package name.bizna.emu65816.opcode;

import name.bizna.emu65816.Address;
import name.bizna.emu65816.Binary;
import name.bizna.emu65816.Cpu65816;
import name.bizna.emu65816.addressingmode.InstructionCycles;

import static name.bizna.emu65816.OpCodeName.*;

public class OpCode_STX
    extends OpCode
{
  public OpCode_STX(int mCode, InstructionCycles cycles)
  {
    super("STX", "Store Index X in Memory", mCode, cycles);
  }

  @Override
  public void executeOnFallingEdge(Cpu65816 cpu)
  {
    Address dataAddress = cpu.getAddressOfOpCodeData(getAddressingMode());
    if (cpu.isMemory8Bit())
    {
      cpu.storeByte(dataAddress, Binary.getLowByte(cpu.getX()));
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
        if (Binary.getLowByte(cpu.getDirectPage()) != 0)
        {
          cpu.addToCycles(1);
        }

        cpu.addToProgramAddress(2);
        cpu.addToCycles(3);
        break;
      }
      case STX_DirectPageIndexedWithY:  // STX Direct Page Indexed, Y
      {
        if (Binary.getLowByte(cpu.getDirectPage()) != 0)
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

