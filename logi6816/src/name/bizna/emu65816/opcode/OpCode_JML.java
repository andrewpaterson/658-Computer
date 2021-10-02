package name.bizna.emu65816.opcode;

import name.bizna.emu65816.Address;
import name.bizna.emu65816.Cpu65816;
import name.bizna.emu65816.addressingmode.InstructionCycles;

import static name.bizna.emu65816.OpCodeName.*;

public class OpCode_JMP
    extends OpCode
{
  public OpCode_JMP(int mCode, InstructionCycles cycles)
  {
    super("JMP", "Jump Long"mCode, cycles);
  }

  @Override
  public void executeOnFallingEdge(Cpu65816 cpu)
  {
    switch (getCode())
    {
      case JMP_Absolute:  // JMP Absolute
      {
        int destinationAddress = cpu.getAddressOfOpCodeData(getAddressingMode()).getOffset();
        cpu.setProgramAddress(new Address(cpu.getProgramCounter().getBank(), destinationAddress));
        cpu.addToCycles(3);
        break;
      }
      case JMP_AbsoluteIndirect:  // JMP Absolute Indirect
      {
        cpu.setProgramAddress(cpu.getAddressOfOpCodeData(getAddressingMode()));
        cpu.addToCycles(5);
        break;
      }
      case JMP_AbsoluteIndexedIndirectWithX:  // JMP Absolute Indexed Indirect, X
      {
        cpu.setProgramAddress(cpu.getAddressOfOpCodeData(getAddressingMode()));
        cpu.addToCycles(6);
        break;
      }
      case JMP_AbsoluteLong:  // JMP Absolute Long
      {
        cpu.setProgramAddress(cpu.getAddressOfOpCodeData(getAddressingMode()));
        cpu.addToCycles(4);
        break;
      }
      case JMP_AbsoluteIndirectLong:  // JMP Absolute Indirect Long
      {
        cpu.setProgramAddress(cpu.getAddressOfOpCodeData(getAddressingMode()));
        cpu.addToCycles(6);
        break;
      }
      default:
        throw new IllegalStateException("Unexpected value: " + getCode());
    }
  }
}

