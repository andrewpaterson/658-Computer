package name.bizna.emu65816.opcode;

import name.bizna.emu65816.Address;
import name.bizna.emu65816.AddressingMode;
import name.bizna.emu65816.Binary;
import name.bizna.emu65816.Cpu65816;

public abstract class OpCodeBranch
    extends OpCode
{
  public OpCodeBranch(String mName, byte mCode, AddressingMode mAddressingMode)
  {
    super(mName, mCode, mAddressingMode);
  }

  int executeBranchShortOnCondition(boolean condition, Cpu65816 cpu)
  {
    byte opCycles = 2;
    byte destination = cpu.readByte(cpu.getAddressOfOpCodeData(getAddressingMode()));
    // This is the address of the next instruction
    short actualDestination;
    if (condition)
    {
      // One extra cycle if the branch is taken
      opCycles++;
      short destination16;
      if (Binary.is8bitValueNegative(destination))
      {
        destination16 = (short) (0xFF00 | destination);
      }
      else
      {
        destination16 = destination;
      }
      actualDestination = (short) (cpu.getProgramAddress().getOffset() + 2 + destination16);
      // Emulation mode requires 1 extra cycle on page boundary crossing
      if (Address.offsetsAreOnDifferentPages(cpu.getProgramAddress().getOffset(), actualDestination) &&
          cpu.getCpuStatus().emulationFlag())
      {
        opCycles++;
      }
    }
    else
    {
      actualDestination = (short) (cpu.getProgramAddress().getOffset() + 2);
    }
    Address newProgramAddress = new Address(cpu.getProgramAddress().getBank(), actualDestination);
    cpu.setProgramAddress(newProgramAddress);
    return opCycles;
  }

  int executeBranchLongOnCondition(boolean condition, Cpu65816 cpu)
  {
    if (condition)
    {
      short destination = cpu.readTwoBytes(cpu.getAddressOfOpCodeData(getAddressingMode()));
      cpu.getProgramAddress().incrementOffsetBy((short) (3 + destination));
    }
    // CPU cycles: 4
    return 4;
  }
}

