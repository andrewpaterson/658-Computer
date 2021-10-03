package name.bizna.emu65816.opcode;

import name.bizna.emu65816.Address;
import name.bizna.emu65816.Binary;
import name.bizna.emu65816.Cpu65816;
import name.bizna.emu65816.addressingmode.InstructionCycles;

import static name.bizna.emu65816.OpCodeName.STZ_Absolute;
import static name.bizna.emu65816.OpCodeName.STZ_AbsoluteIndexedWithX;

public class OpCode_STZ
    extends OpCode
{
  public OpCode_STZ(int mCode, InstructionCycles cycles)
  {
    super("STZ", "Store Zero in Memory", mCode, cycles);
  }

  @Override
  public void executeOnFallingEdge(Cpu65816 cpu)
  {
    Address dataAddress = cpu.getAddressOfOpCodeData(getAddressingMode());
    if (cpu.isMemory8Bit())
    {
      cpu.storeByte(dataAddress, 0x00);
    }
    else
    {
      cpu.storeTwoBytes(dataAddress, 0x0000);
      cpu.addToCycles(1);
    }

    switch (getCode())
    {
      case STZ_Absolute:  // STZ Absolute
      {
        cpu.addToProgramAddress(3);
        cpu.addToCycles(4);
        break;
      }
      case (0x64):  // STZ Direct Page
      {
        if (Binary.getLowByte(cpu.getDirectPage()) != 0)
        {
          cpu.addToCycles(1);
        }

        cpu.addToProgramAddress(2);
        cpu.addToCycles(3);
        break;
      }
      case STZ_AbsoluteIndexedWithX:  // STZ Absolute Indexed, X
      {
        cpu.addToProgramAddress(3);
        cpu.addToCycles(5);
        break;
      }
      case (0x74):  // STZ Direct Page Indexed, X
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

