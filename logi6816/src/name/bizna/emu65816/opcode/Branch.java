package name.bizna.emu65816.opcode;

import name.bizna.emu65816.Address;
import name.bizna.emu65816.Binary;
import name.bizna.emu65816.Cpu65816;

import static name.bizna.emu65816.Unsigned.toShort;

public class Branch
{
  public static int executeBranchShortOnCondition(boolean condition, Cpu65816 cpu)
  {
    int opCycles = 2;
    int destination = 0;
    // This is the address of the next instruction
    int actualDestination;
    if (condition)
    {
      // One extra cycle if the branch is taken
      opCycles++;
      int destination16;
      if (Binary.is8bitValueNegative(destination))
      {
        destination16 = toShort(0xFF00 | destination);
      }
      else
      {
        destination16 = destination;
      }
      actualDestination = toShort(cpu.getProgramCounter().getOffset() + 2 + destination16);
      // Emulation mode requires 1 extra cycle on page boundary crossing
      if (Address.areOffsetsAreOnDifferentPages(cpu.getProgramCounter().getOffset(), actualDestination) &&
          cpu.getCpuStatus().isEmulationMode())
      {
        opCycles++;
      }
    }
    else
    {
      actualDestination = toShort(cpu.getProgramCounter().getOffset() + 2);
    }
    Address newProgramAddress = new Address(cpu.getProgramCounter().getBank(), actualDestination);
    cpu.setProgramAddress(newProgramAddress);
    return opCycles;
  }
}

