package name.bizna.cpu.addressingmode;

import name.bizna.cpu.Cpu65816;
import name.bizna.cpu.Width;

import java.util.function.Consumer;

import static name.bizna.cpu.AddressingMode.DirectIndexedIndirectWithX;

public class DirectIndexedIndirectWithXWriteCycles
    extends InstructionCycles
{
  //11
  public DirectIndexedIndirectWithXWriteCycles(Consumer<Cpu65816> operation)
  {
    super(DirectIndexedIndirectWithX,
          new BusCycle(Address(PBR(), PC()), OpCode(), PC_inc()),
          new BusCycle(Address(PBR(), PC()), Read_D0(), PC_inc()),
          new BusCycle(Address(PBR(), PC()), IO(), new NoteTwo()),
          new BusCycle(Address(PBR(), PC()), IO()),
          new BusCycle(Address(DP(), D0(), X()), Read_AAL()),
          new BusCycle(Address(DP(), D0(), X(), o(1)), Read_AAH()),
          new BusCycle(Address(DBR(), AA()), E(operation), Write_DataLow(), DONE8Bit(Width.A)),
          new BusCycle(Address(DBR(), AA(), o(1)), Write_DataHigh(), DONE16Bit(Width.A)));
  }
}

