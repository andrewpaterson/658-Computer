package name.bizna.emu65816.addressingmode;

import name.bizna.emu65816.Cpu65816;

import java.util.function.Consumer;

import static name.bizna.emu65816.AddressingMode.Immediate;

public class ImmediateREPSEPCycles
    extends InstructionCycles
{
  //18
  public ImmediateREPSEPCycles(Consumer<Cpu65816> operation)
  {
    //The instruction must work out when to go to the next instruction on execute.  REP and SEP are an 8bit problem.
    super(Immediate,
          new BusCycle(Address(PBR(), PC()), OpCode(), PC_inc()),
          new BusCycle(Address(PBR(), PC()), PC_inc(), Read_DataLow()),
          new BusCycle(Address(PBR(), PC()), PC_inc(), Read_DataHigh(), E(operation)));
  }
}
