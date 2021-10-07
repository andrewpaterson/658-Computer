package name.bizna.cpu.addressingmode;

import name.bizna.cpu.Cpu65816;

import java.util.function.Consumer;

import static name.bizna.cpu.AddressingMode.Stack;

public class StackRTICycles
    extends InstructionCycles
{
  //22g
  public StackRTICycles(Consumer<Cpu65816> operation)
  {
    super(Stack,
          new BusCycle(Address(PBR(), PC()), OpCode(), PC_inc()),
          new BusCycle(Address(PBR(), PC()), IO(), PC_inc()),
          new BusCycle(Address(PBR(), PC()), IO()),
          new BusCycle(Address(S(), o(1)), Read_DataLow(), SP_inc()),  //Processor status
          new BusCycle(Address(S(), o(1)), Read_NewPCL(), SP_inc()),
          new BusCycle(Address(S(), o(1)), Read_NewPCH(), SP_inc()),
          new BusCycle(Address(S(), o(1)), Read_NewPBR(), E(operation), SP_inc(), PC_e(New_PBR(), New_PC()), DONE()));  //Set processor status from data low.
  }
}

