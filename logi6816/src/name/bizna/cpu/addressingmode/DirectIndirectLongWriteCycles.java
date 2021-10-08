package name.bizna.cpu.addressingmode;

import name.bizna.cpu.Cpu65816;

import java.util.function.Consumer;

import static name.bizna.cpu.AddressingMode.DirectIndirectLong;
import static name.bizna.cpu.WidthFromRegister.A;

public class DirectIndirectLongWriteCycles
    extends InstructionCycles
{
  //15
  public DirectIndirectLongWriteCycles(Consumer<Cpu65816> operation)
  {
    super(DirectIndirectLong,
          new BusCycle(Address(PBR(), PC()), OpCode(), PC_inc()),
          new BusCycle(Address(PBR(), PC()), Read_D0(), PC_inc()),
          new BusCycle(Address(PBR(), PC()), IO(), new NoteTwo()),
          new BusCycle(Address(DP(), D0()), Read_AAL()),
          new BusCycle(Address(DP(), D0(), o(1)), Read_AAH()),
          new BusCycle(Address(DP(), D0(), o(2)), Read_AAB()),
          new BusCycle(Address(AAB(), AA()), E(operation), Write_DataLow(), DONE8Bit(A)),
          new BusCycle(Address(AAB(), AA(), o(1)), Write_DataHigh(), DONE16Bit(A)));
  }
}

