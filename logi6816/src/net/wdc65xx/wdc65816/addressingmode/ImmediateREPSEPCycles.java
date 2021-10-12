package net.wdc65xx.wdc65816.addressingmode;

import net.wdc65xx.wdc65816.Cpu65816;
import net.wdc65xx.wdc65816.Executor;

import static net.wdc65xx.wdc65816.AddressingMode.Immediate;

public class ImmediateREPSEPCycles
    extends InstructionCycles
{
  //18
  public ImmediateREPSEPCycles(Executor<Cpu65816> operation)
  {
    //The instruction must work out when to go to the next instruction on execute.  REP and SEP are an 8bit problem.
    super(Immediate,
          new BusCycle(Address(PBR(), PC()), OpCode(), PC_inc()),
          new BusCycle(Address(PBR(), PC()), PC_inc(), Read_DataLow()),
          new BusCycle(Address(PBR(), PC()), Read_DataHigh(), E(operation), DONE()));
  }
}

