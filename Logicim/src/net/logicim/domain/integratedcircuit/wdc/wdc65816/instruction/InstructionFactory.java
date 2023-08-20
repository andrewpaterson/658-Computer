package net.logicim.domain.integratedcircuit.wdc.wdc65816.instruction;

import net.logicim.common.SimulatorException;
import net.logicim.domain.integratedcircuit.wdc.wdc65816.W65C816;
import net.logicim.domain.integratedcircuit.wdc.wdc65816.instruction.address.InstructionCycles;
import net.logicim.domain.integratedcircuit.wdc.wdc65816.instruction.interrupt.*;

import java.util.ArrayList;
import java.util.List;

import static net.logicim.domain.integratedcircuit.wdc.wdc65816.WidthFromRegister.M;
import static net.logicim.domain.integratedcircuit.wdc.wdc65816.WidthFromRegister.XY;
import static net.logicim.domain.integratedcircuit.wdc.wdc65816.instruction.InstructionCodes.*;
import static net.logicim.domain.integratedcircuit.wdc.wdc65816.instruction.address.InstructionCycleFactory.*;

@SuppressWarnings({"SameParameterValue"})
public class InstructionFactory
{
  public static Instruction[] createInstructions()
  {
    List<Instruction> opCodes = new ArrayList<>();

    opCodes.add(createBrk(BRK_Interrupt, createStackSoftwareInterruptCycles(new BRKVector(), W65C816::BRK)));
    opCodes.add(createORA(ORA_DirectPageIndexedIndirectWithX, createDirectIndexedIndirectWithXCycles(W65C816::ORA)));
    opCodes.add(createCOP(COP_Interrupt, createStackSoftwareInterruptCycles(new COPVector(), W65C816::COP)));
    opCodes.add(createORA(ORA_StackRelative, createStackRelativeCycles(W65C816::ORA)));
    opCodes.add(createTSB(TSB_DirectPage, createDirectRMWCycles(W65C816::TSB)));
    opCodes.add(createORA(ORA_DirectPage, createDirectCycles(W65C816::ORA, M)));
    opCodes.add(createASL(ASL_DirectPage, createDirectRMWCycles(W65C816::ASL)));
    opCodes.add(createORA(ORA_DirectPageIndirectLong, createDirectIndirectLongCycles(W65C816::ORA)));
    opCodes.add(createPHP(PHP_StackImplied, createStackImpliedPHPCycles(W65C816::PHP)));
    opCodes.add(createORA(ORA_Immediate, createImmediateCycles(W65C816::ORA, M)));
    opCodes.add(createSLA(ASL_Accumulator, createAccumulatorCycles(W65C816::ASL_A)));
    opCodes.add(createPHD(PHD_StackImplied, createStackPHDCycles(W65C816::PHD)));
    opCodes.add(createTSB(TSB_Absolute, createAbsoluteRMWCycles(W65C816::TSB)));
    opCodes.add(createORA(ORA_Absolute, createAbsoluteCycles(W65C816::ORA, M)));
    opCodes.add(createASL(ASL_Absolute, createAbsoluteRMWCycles(W65C816::ASL)));
    opCodes.add(createORA(ORA_AbsoluteLong, createAbsoluteLongCycles(W65C816::ORA)));
    opCodes.add(createBPL(BPL_ProgramCounterRelative, createRelativeShortCycles(W65C816::BPL)));
    opCodes.add(createORA(ORA_DirectPageIndirectIndexedWithY, createDirectIndirectIndexedWithYCycles(W65C816::ORA)));
    opCodes.add(createORA(ORA_DirectPageIndirect, createDirectIndirectCycles(W65C816::ORA)));
    opCodes.add(createORA(ORA_StackRelativeIndirectIndexedWithY, createStackRelativeIndirectIndexedWithYCycles(W65C816::ORA)));
    opCodes.add(createTRB(TRB_DirectPage, createDirectRMWCycles(W65C816::TRB)));
    opCodes.add(createORA(ORA_DirectPageIndexedWithX, createDirectIndexedWithXCycles(W65C816::ORA, M)));
    opCodes.add(createASL(ASL_DirectPageIndexedWithX, createDirectIndexedWithXRMWCycles(W65C816::ASL)));
    opCodes.add(createORA(ORA_DirectPageIndirectLongIndexedWithY, createDirectIndirectLongIndexedWithYCycles(W65C816::ORA)));
    opCodes.add(createCLC(CLC_Implied, createImpliedCycles(W65C816::CLC)));
    opCodes.add(createORA(ORA_AbsoluteIndexedWithY, createAbsoluteIndexedWithYCycles(W65C816::ORA, M)));
    opCodes.add(createINA(INC_Accumulator, createAccumulatorCycles(W65C816::INC_A)));
    opCodes.add(createTCS(TCS_Implied, createImpliedCycles(W65C816::TCS)));
    opCodes.add(createTRB(TRB_Absolute, createAbsoluteRMWCycles(W65C816::TRB)));
    opCodes.add(createORA(ORA_AbsoluteIndexedWithX, createAbsoluteIndexedWithXCycles(W65C816::ORA, M)));
    opCodes.add(createASL(ASL_AbsoluteIndexedWithX, createAbsoluteIndexedWithXRMWCycles(W65C816::ASL)));
    opCodes.add(createORA(ORA_AbsoluteLongIndexedWithX, createAbsoluteLongIndexedWithXCycles(W65C816::ORA)));
    opCodes.add(createJSR(JSR_Absolute, createAbsoluteJSRCycles()));
    opCodes.add(createAND(AND_DirectPageIndexedIndirectWithX, createDirectIndexedIndirectWithXCycles(W65C816::AND)));
    opCodes.add(createJSL(JSL_AbsoluteLong, createAbsoluteLongJSLCycles()));
    opCodes.add(createAND(AND_StackRelative, createStackRelativeCycles(W65C816::AND)));
    opCodes.add(createBIT(BIT_DirectPage, createDirectCycles(W65C816::BIT, M)));
    opCodes.add(createAND(AND_DirectPage, createDirectCycles(W65C816::AND, M)));
    opCodes.add(createROL(ROL_DirectPage, createDirectRMWCycles(W65C816::ROL)));
    opCodes.add(createAND(AND_DirectPageIndirectLong, createDirectIndirectLongCycles(W65C816::AND)));
    opCodes.add(createPLP(PLP_StackImplied, createStackPLPCycles(W65C816::PLP)));
    opCodes.add(createAND(AND_Immediate, createImmediateCycles(W65C816::AND, M)));
    opCodes.add(createRLA(ROL_Accumulator, createAccumulatorCycles(W65C816::ROL_A)));
    opCodes.add(createPLD(PLD_StackImplied, createStackPLDCycles(W65C816::PLD)));
    opCodes.add(createBIT(BIT_Absolute, createAbsoluteCycles(W65C816::BIT, M)));
    opCodes.add(createAND(AND_Absolute, createAbsoluteCycles(W65C816::AND, M)));
    opCodes.add(createROL(ROL_Absolute, createAbsoluteRMWCycles(W65C816::ROL)));
    opCodes.add(createAND(AND_AbsoluteLong, createAbsoluteLongCycles(W65C816::AND)));
    opCodes.add(createBMI(BMI_ProgramCounterRelative, createRelativeShortCycles(W65C816::BMI)));
    opCodes.add(createAND(AND_DirectPageIndirectIndexedWithY, createDirectIndirectIndexedWithYCycles(W65C816::AND)));
    opCodes.add(createAND(AND_DirectPageIndirect, createDirectIndirectCycles(W65C816::AND)));
    opCodes.add(createAND(AND_StackRelativeIndirectIndexedWithY, createStackRelativeIndirectIndexedWithYCycles(W65C816::AND)));
    opCodes.add(createBIT(BIT_DirectPageIndexedWithX, createDirectIndexedWithXCycles(W65C816::BIT, M)));
    opCodes.add(createAND(AND_DirectPageIndexedWithX, createDirectIndexedWithXCycles(W65C816::AND, M)));
    opCodes.add(createROL(ROL_DirectPageIndexedWithX, createDirectIndexedWithXRMWCycles(W65C816::ROL)));
    opCodes.add(createAND(AND_DirectPageIndirectLongIndexedWithY, createDirectIndirectLongIndexedWithYCycles(W65C816::AND)));
    opCodes.add(createSEC(SEC_Implied, createImpliedCycles(W65C816::SEC)));
    opCodes.add(createAND(AND_AbsoluteIndexedWithY, createAbsoluteIndexedWithYCycles(W65C816::AND, M)));
    opCodes.add(createDEA(DEC_Accumulator, createAccumulatorCycles(W65C816::DEC_A)));
    opCodes.add(createTSC(TSC_Implied, createImpliedCycles(W65C816::TSC)));
    opCodes.add(createBIT(BIT_AbsoluteIndexedWithX, createAbsoluteIndexedWithXCycles(W65C816::BIT, M)));
    opCodes.add(createAND(AND_AbsoluteIndexedWithX, createAbsoluteIndexedWithXCycles(W65C816::AND, M)));
    opCodes.add(createROL(ROL_AbsoluteIndexedWithX, createAbsoluteIndexedWithXRMWCycles(W65C816::ROL)));
    opCodes.add(createAND(AND_AbsoluteLongIndexedWithX, createAbsoluteLongIndexedWithXCycles(W65C816::AND)));
    opCodes.add(createRTI(RTI_StackImplied, createStackRTICycles()));
    opCodes.add(createEOR(EOR_DirectPageIndexedIndirectWithX, createDirectIndexedIndirectWithXCycles(W65C816::EOR)));
    opCodes.add(createWDM(WDM_Implied, createImpliedCycles(W65C816::WDM)));
    opCodes.add(createEOR(EOR_StackRelative, createStackRelativeCycles(W65C816::EOR)));
    opCodes.add(createMVP(MVP_BlockMove, createBlockMoveCycles(W65C816::MVP)));
    opCodes.add(createEOR(EOR_DirectPage, createDirectCycles(W65C816::EOR, M)));
    opCodes.add(createLSR(LSR_DirectPage, createDirectRMWCycles(W65C816::LSR)));
    opCodes.add(createEOR(EOR_DirectPageIndirectLong, createDirectIndirectLongCycles(W65C816::EOR)));
    opCodes.add(createPHA(PHA_StackImplied, createStackPushCycles(W65C816::PHA, M)));
    opCodes.add(createEOR(EOR_Immediate, createImmediateCycles(W65C816::EOR, M)));
    opCodes.add(createSRA(LSR_Accumulator, createAccumulatorCycles(W65C816::LSR_A)));
    opCodes.add(createPHK(PHK_StackImplied, createStackPHKCycles(W65C816::PHK)));
    opCodes.add(createJMP(JMP_Absolute, createAbsoluteJMPCycles()));
    opCodes.add(createEOR(EOR_Absolute, createAbsoluteCycles(W65C816::EOR, M)));
    opCodes.add(createLSR(LSR_Absolute, createAbsoluteRMWCycles(W65C816::LSR)));
    opCodes.add(createEOR(EOR_AbsoluteLong, createAbsoluteLongCycles(W65C816::EOR)));
    opCodes.add(createBVC(BVC_ProgramCounterRelative, createRelativeShortCycles(W65C816::BVC)));
    opCodes.add(createEOR(EOR_DirectPageIndirectIndexedWithY, createDirectIndirectIndexedWithYCycles(W65C816::EOR)));
    opCodes.add(createEOR(EOR_DirectPageIndirect, createDirectIndirectCycles(W65C816::EOR)));
    opCodes.add(createEOR(EOR_StackRelativeIndirectIndexedWithY, createStackRelativeIndirectIndexedWithYCycles(W65C816::EOR)));
    opCodes.add(createMVN(MVN_BlockMove, createBlockMoveCycles(W65C816::MVN)));
    opCodes.add(createEOR(EOR_DirectPageIndexedWithX, createDirectIndexedWithXCycles(W65C816::EOR, M)));
    opCodes.add(createLSR(LSR_DirectPageIndexedWithX, createDirectIndexedWithXRMWCycles(W65C816::LSR)));
    opCodes.add(createEOR(EOR_DirectPageIndirectLongIndexedWithY, createDirectIndirectLongIndexedWithYCycles(W65C816::EOR)));
    opCodes.add(createCLI(CLI_Implied, createImpliedCycles(W65C816::CLI)));
    opCodes.add(createEOR(EOR_AbsoluteIndexedWithY, createAbsoluteIndexedWithYCycles(W65C816::EOR, M)));
    opCodes.add(createPHY(PHY_StackImplied, createStackPushCycles(W65C816::PHY, XY)));
    opCodes.add(createTCD(TCD_Implied, createImpliedCycles(W65C816::TCD)));
    opCodes.add(createJMP(JML_AbsoluteLong, createAbsoluteLongJMLCycles()));
    opCodes.add(createEOR(EOR_AbsoluteIndexedWithX, createAbsoluteIndexedWithXCycles(W65C816::EOR, M)));
    opCodes.add(createLSR(LSR_AbsoluteIndexedWithX, createAbsoluteIndexedWithXRMWCycles(W65C816::LSR)));
    opCodes.add(createEOR(EOR_AbsoluteLongIndexedWithX, createAbsoluteLongIndexedWithXCycles(W65C816::EOR)));
    opCodes.add(createRTS(RTS_StackImplied, createStackRTSCycles()));
    opCodes.add(createADC(ADC_DirectPageIndexedIndirectWithX, createDirectIndexedIndirectWithXCycles(W65C816::ADC)));
    opCodes.add(createPER(PER_StackProgramCounterRelativeLong, createStackPERCycles(W65C816::PER)));
    opCodes.add(createADC(ADC_StackRelative, createStackRelativeCycles(W65C816::ADC)));
    opCodes.add(createSTZ(STZ_DirectPage, createDirectWriteCycles(W65C816::STZ, M)));
    opCodes.add(createADC(ADC_DirectPage, createDirectCycles(W65C816::ADC, M)));
    opCodes.add(createROR(ROR_DirectPage, createDirectRMWCycles(W65C816::ROR)));
    opCodes.add(createADC(ADC_DirectPageIndirectLong, createDirectIndirectLongCycles(W65C816::ADC)));
    opCodes.add(createPLA(PLA_Stack, createStackPullCycles(W65C816::PLA, M)));
    opCodes.add(createADC(ADC_Immediate, createImmediateCycles(W65C816::ADC, M)));
    opCodes.add(createRRA(ROR_Accumulator, createAccumulatorCycles(W65C816::ROR_A)));
    opCodes.add(createRTL(RTL_StackImplied, createStackRTLCycles()));
    opCodes.add(createJMP(JMP_AbsoluteIndirect, createAbsoluteIndirectJMPCycles()));
    opCodes.add(createADC(ADC_Absolute, createAbsoluteCycles(W65C816::ADC, M)));
    opCodes.add(createROR(ROR_Absolute, createAbsoluteRMWCycles(W65C816::ROR)));
    opCodes.add(createADC(ADC_AbsoluteLong, createAbsoluteLongCycles(W65C816::ADC)));
    opCodes.add(createBVS(BVS_ProgramCounterRelative, createRelativeShortCycles(W65C816::BVS)));
    opCodes.add(createADC(ADC_DirectPageIndirectIndexedWithY, createDirectIndirectIndexedWithYCycles(W65C816::ADC)));
    opCodes.add(createADC(ADC_DirectPageIndirect, createDirectIndirectCycles(W65C816::ADC)));
    opCodes.add(createADC(ADC_StackRelativeIndirectIndexedWithY, createStackRelativeIndirectIndexedWithYCycles(W65C816::ADC)));
    opCodes.add(createSTZ(STZ_DirectPageIndexedWithX, createDirectIndexedWithXWriteCycles(W65C816::STZ, M)));
    opCodes.add(createADC(ADC_DirectPageIndexedWithX, createDirectIndexedWithXCycles(W65C816::ADC, M)));
    opCodes.add(createROR(ROR_DirectPageIndexedWithX, createDirectIndexedWithXRMWCycles(W65C816::ROR)));
    opCodes.add(createADC(ADC_DirectPageIndirectLongIndexedWithY, createDirectIndirectLongIndexedWithYCycles(W65C816::ADC)));
    opCodes.add(createSEI(SEI_Implied, createImpliedCycles(W65C816::SEI)));
    opCodes.add(createADC(ADC_AbsoluteIndexedWithY, createAbsoluteIndexedWithYCycles(W65C816::ADC, M)));
    opCodes.add(createPLY(PLY_StackImplied, createStackPullCycles(W65C816::PLY, XY)));
    opCodes.add(createTDC(TDC_Implied, createImpliedCycles(W65C816::TDC)));
    opCodes.add(createJMP(JMP_AbsoluteIndexedIndirectWithX, createAbsoluteIndexedIndirectWithXJMPCycles()));
    opCodes.add(createADC(ADC_AbsoluteIndexedWithX, createAbsoluteIndexedWithXCycles(W65C816::ADC, M)));
    opCodes.add(createROR(ROR_AbsoluteIndexedWithX, createAbsoluteIndexedWithXRMWCycles(W65C816::ROR)));
    opCodes.add(createADC(ADC_AbsoluteLongIndexedWithX, createAbsoluteLongIndexedWithXCycles(W65C816::ADC)));
    opCodes.add(createBRA(BRA_ProgramCounterRelative, createRelativeShortCycles(W65C816::BRA)));
    opCodes.add(createSTA(STA_DirectPageIndexedIndirectWithX, createDirectIndexedIndirectWithXWriteCycles(W65C816::STA)));
    opCodes.add(createBRL(BRL_ProgramCounterRelativeLong, createRelativeLongCycles(W65C816::BRA)));
    opCodes.add(createSTA(STA_StackRelative, createStackRelativeWriteCycles(W65C816::STA)));
    opCodes.add(createSTY(STY_DirectPage, createDirectWriteCycles(W65C816::STY, XY)));
    opCodes.add(createSTA(STA_DirectPage, createDirectWriteCycles(W65C816::STA, M)));
    opCodes.add(createSTX(STX_DirectPage, createDirectWriteCycles(W65C816::STX, XY)));
    opCodes.add(createSTA(STA_DirectPageIndirectLong, createDirectIndirectLongWriteCycles(W65C816::STA)));
    opCodes.add(createDEY(DEY_Implied, createImpliedCycles(W65C816::DEY)));
    opCodes.add(createBIT(BIT_Immediate, createImmediateCycles(W65C816::BIT_I, M)));
    opCodes.add(createTXA(TXA_Implied, createImpliedCycles(W65C816::TXA)));
    opCodes.add(createPHB(PHB_StackImplied, createStackPHBCycles(W65C816::PHB)));
    opCodes.add(createSTY(STY_Absolute, createAbsoluteWriteCycles(W65C816::STY, XY)));
    opCodes.add(createSTA(STA_Absolute, createAbsoluteWriteCycles(W65C816::STA, M)));
    opCodes.add(createSTX(STX_Absolute, createAbsoluteWriteCycles(W65C816::STX, XY)));
    opCodes.add(createSTA(STA_AbsoluteLong, createAbsoluteLongWriteCycles(W65C816::STA)));
    opCodes.add(createBCC(BCC_ProgramCounterRelative, createRelativeShortCycles(W65C816::BCC)));
    opCodes.add(createSTA(STA_DirectPageIndirectIndexedWithY, createDirectIndirectIndexedWithYWriteCycles(W65C816::STA)));
    opCodes.add(createSTA(STA_DirectPageIndirect, createDirectIndirectWriteCycles(W65C816::STA)));
    opCodes.add(createSTA(STA_StackRelativeIndirectIndexedWithY, createStackRelativeIndirectIndexedWithYWriteCycles(W65C816::STA)));
    opCodes.add(createSTY(STY_DirectPageIndexedWithX, createDirectIndexedWithXWriteCycles(W65C816::STY, XY)));
    opCodes.add(createSTA(STA_DirectPageIndexedWithX, createDirectIndexedWithXWriteCycles(W65C816::STA, M)));
    opCodes.add(createSTX(STX_DirectPageIndexedWithY, createDirectIndexedWithYWriteCycles(W65C816::STX, XY)));
    opCodes.add(createSTA(STA_DirectPageIndirectLongIndexedWithY, createDirectIndirectLongIndexedWithYWriteCycles(W65C816::STA)));
    opCodes.add(createTYA(TYA_Implied, createImpliedCycles(W65C816::TYA)));
    opCodes.add(createSTA(STA_AbsoluteIndexedWithY, createAbsoluteIndexedWithYWriteCycles(W65C816::STA)));
    opCodes.add(createTXS(TXS_Implied, createImpliedCycles(W65C816::TXS)));
    opCodes.add(createTXY(TXY_Implied, createImpliedCycles(W65C816::TXY)));
    opCodes.add(createSTZ(STZ_Absolute, createAbsoluteWriteCycles(W65C816::STZ, M)));
    opCodes.add(createSTA(STA_AbsoluteIndexedWithX, createAbsoluteIndexedWithXWriteCycles(W65C816::STA)));
    opCodes.add(createSTZ(STZ_AbsoluteIndexedWithX, createAbsoluteIndexedWithXWriteCycles(W65C816::STZ)));
    opCodes.add(createSTA(STA_AbsoluteLongIndexedWithX, createAbsoluteLongIndexedWithXWriteCycles(W65C816::STA)));
    opCodes.add(createLDY(LDY_Immediate, createImmediateCycles(W65C816::LDY, XY)));
    opCodes.add(createLDA(LDA_DirectPageIndexedIndirectWithX, createDirectIndexedIndirectWithXCycles(W65C816::LDA)));
    opCodes.add(createLDX(LDX_Immediate, createImmediateCycles(W65C816::LDX, XY)));
    opCodes.add(createLDA(LDA_StackRelative, createStackRelativeCycles(W65C816::LDA)));
    opCodes.add(createLDY(LDY_DirectPage, createDirectCycles(W65C816::LDY, XY)));
    opCodes.add(createLDA(LDA_DirectPage, createDirectCycles(W65C816::LDA, M)));
    opCodes.add(createLDX(LDX_DirectPage, createDirectCycles(W65C816::LDX, XY)));
    opCodes.add(createLDA(LDA_DirectPageIndirectLong, createDirectIndirectLongCycles(W65C816::LDA)));
    opCodes.add(createTAY(TAY_Implied, createImpliedCycles(W65C816::TAY)));
    opCodes.add(createLDA(LDA_Immediate, createImmediateCycles(W65C816::LDA, M)));
    opCodes.add(createTAX(TAX_Implied, createImpliedCycles(W65C816::TAX)));
    opCodes.add(createPLB(PLB_StackImplied, createStackPLBCycles(W65C816::PLB)));
    opCodes.add(createLDY(LDY_Absolute, createAbsoluteCycles(W65C816::LDY, XY)));
    opCodes.add(createLDA(LDA_Absolute, createAbsoluteCycles(W65C816::LDA, M)));
    opCodes.add(createLDX(LDX_Absolute, createAbsoluteCycles(W65C816::LDX, XY)));
    opCodes.add(createLDA(LDA_AbsoluteLong, createAbsoluteLongCycles(W65C816::LDA)));
    opCodes.add(createBCS(BCS_ProgramCounterRelative, createRelativeShortCycles(W65C816::BCS)));
    opCodes.add(createLDA(LDA_DirectPageIndirectIndexedWithY, createDirectIndirectIndexedWithYCycles(W65C816::LDA)));
    opCodes.add(createLDA(LDA_DirectPageIndirect, createDirectIndirectCycles(W65C816::LDA)));
    opCodes.add(createLDA(LDA_StackRelativeIndirectIndexedWithY, createStackRelativeIndirectIndexedWithYCycles(W65C816::LDA)));
    opCodes.add(createLDY(LDY_DirectPageIndexedWithX, createDirectIndexedWithXCycles(W65C816::LDY, XY)));
    opCodes.add(createLDA(LDA_DirectPageIndexedWithX, createDirectIndexedWithXCycles(W65C816::LDA, M)));
    opCodes.add(createLDX(LDX_DirectPageIndexedWithY, createDirectIndexedWithYCycles(W65C816::LDX, XY)));
    opCodes.add(createLDA(LDA_DirectPageIndirectLongIndexedWithY, createDirectIndirectLongIndexedWithYCycles(W65C816::LDA)));
    opCodes.add(createCLV(CLV_Implied, createImpliedCycles(W65C816::CLV)));
    opCodes.add(createLDA(LDA_AbsoluteIndexedWithY, createAbsoluteIndexedWithYCycles(W65C816::LDA, M)));
    opCodes.add(createTSX(TSX_Implied, createImpliedCycles(W65C816::TSX)));
    opCodes.add(createTYX(TYX_Implied, createImpliedCycles(W65C816::TYX)));
    opCodes.add(createLDY(LDY_AbsoluteIndexedWithX, createAbsoluteIndexedWithXCycles(W65C816::LDY, XY)));
    opCodes.add(createLDA(LDA_AbsoluteIndexedWithX, createAbsoluteIndexedWithXCycles(W65C816::LDA, M)));
    opCodes.add(createLDX(LDX_AbsoluteIndexedWithY, createAbsoluteIndexedWithYCycles(W65C816::LDX, XY)));
    opCodes.add(createLDA(LDA_AbsoluteLongIndexedWithX, createAbsoluteLongIndexedWithXCycles(W65C816::LDA)));
    opCodes.add(createCPY(CPY_Immediate, createImmediateCycles(W65C816::CPY, XY)));
    opCodes.add(createCMP(CMP_DirectPageIndexedIndirectWithX, createDirectIndexedIndirectWithXCycles(W65C816::CMP)));
    opCodes.add(createREP(REP_Immediate, createImmediateREPSEPCycles(W65C816::REP)));
    opCodes.add(createCMP(CMP_StackRelative, createStackRelativeCycles(W65C816::CMP)));
    opCodes.add(createCPY(CPY_DirectPage, createDirectCycles(W65C816::CPY, XY)));
    opCodes.add(createCMP(CMP_DirectPage, createDirectCycles(W65C816::CMP, M)));
    opCodes.add(createDEC(DEC_DirectPage, createDirectRMWCycles(W65C816::DEC)));
    opCodes.add(createCMP(CMP_DirectPageIndirectLong, createDirectIndirectLongCycles(W65C816::CMP)));
    opCodes.add(createINY(INY_Implied, createImpliedCycles(W65C816::INY)));
    opCodes.add(createCMP(CMP_Immediate, createImmediateCycles(W65C816::CMP, M)));
    opCodes.add(createDEX(DEX_Implied, createImpliedCycles(W65C816::DEX)));
    opCodes.add(createWAI(WAI_Implied, createWaitForInterruptCycles()));
    opCodes.add(createCPY(CPY_Absolute, createAbsoluteCycles(W65C816::CPY, XY)));
    opCodes.add(createCMP(CMP_Absolute, createAbsoluteCycles(W65C816::CMP, M)));
    opCodes.add(createDEC(DEC_Absolute, createAbsoluteRMWCycles(W65C816::DEC)));
    opCodes.add(createCMP(CMP_AbsoluteLong, createAbsoluteLongCycles(W65C816::CMP)));
    opCodes.add(createBNE(BNE_ProgramCounterRelative, createRelativeShortCycles(W65C816::BNE)));
    opCodes.add(createCMP(CMP_DirectPageIndirectIndexedWithY, createDirectIndirectIndexedWithYCycles(W65C816::CMP)));
    opCodes.add(createCMP(CMP_DirectPageIndirect, createDirectIndirectCycles(W65C816::CMP)));
    opCodes.add(createCMP(CMP_StackRelativeIndirectIndexedWithY, createStackRelativeIndirectIndexedWithYCycles(W65C816::CMP)));
    opCodes.add(createPEI(PEI_StackDirectPageIndirect, createStackPEICycles()));
    opCodes.add(createCMP(CMP_DirectPageIndexedWithX, createDirectIndexedWithXCycles(W65C816::CMP, M)));
    opCodes.add(createDEC(DEC_DirectPageIndexedWithX, createDirectIndexedWithXRMWCycles(W65C816::DEC)));
    opCodes.add(createCMP(CMP_DirectPageIndirectLongIndexedWithY, createDirectIndirectLongIndexedWithYCycles(W65C816::CMP)));
    opCodes.add(createCLD(CLD_Implied, createImpliedCycles(W65C816::CLD)));
    opCodes.add(createCMP(CMP_AbsoluteIndexedWithY, createAbsoluteIndexedWithYCycles(W65C816::CMP, M)));
    opCodes.add(createPHX(PHX_StackImplied, createStackPushCycles(W65C816::PHX, XY)));
    opCodes.add(createSTP(STP_Implied, createStopTheClockCycles(W65C816::STP)));
    opCodes.add(createJMP(JML_AbsoluteIndirectLong, createAbsoluteIndirectJMLCycles()));
    opCodes.add(createCMP(CMP_AbsoluteIndexedWithX, createAbsoluteIndexedWithXCycles(W65C816::CMP, M)));
    opCodes.add(createDEC(DEC_AbsoluteIndexedWithX, createAbsoluteIndexedWithXRMWCycles(W65C816::DEC)));
    opCodes.add(createCMP(CMP_AbsoluteLongIndexedWithX, createAbsoluteLongIndexedWithXCycles(W65C816::CMP)));
    opCodes.add(createCPX(CPX_Immediate, createImmediateCycles(W65C816::CPX, XY)));
    opCodes.add(createSBC(SBC_DirectPageIndexedIndirectWithX, createDirectIndexedIndirectWithXCycles(W65C816::SBC)));
    opCodes.add(createSEP(SEP_Immediate, createImmediateREPSEPCycles(W65C816::SEP)));
    opCodes.add(createSBC(SBC_StackRelative, createStackRelativeCycles(W65C816::SBC)));
    opCodes.add(createCPX(CPX_DirectPage, createDirectCycles(W65C816::CPX, XY)));
    opCodes.add(createSBC(SBC_DirectPage, createDirectCycles(W65C816::SBC, M)));
    opCodes.add(createINC(INC_DirectPage, createDirectRMWCycles(W65C816::INC)));
    opCodes.add(createSBC(SBC_DirectPageIndirectLong, createDirectIndirectLongCycles(W65C816::SBC)));
    opCodes.add(createINX(INX_Implied, createImpliedCycles(W65C816::INX)));
    opCodes.add(createSBC(SBC_Immediate, createImmediateCycles(W65C816::SBC, M)));
    opCodes.add(createNOP(NOP_Implied, createImpliedCycles(W65C816::NOP)));
    opCodes.add(createXBA(XBA_Implied, createImpliedXBACycles(W65C816::XBA)));
    opCodes.add(createCPX(CPX_Absolute, createAbsoluteCycles(W65C816::CPX, XY)));
    opCodes.add(createSBC(SBC_Absolute, createAbsoluteCycles(W65C816::SBC, M)));
    opCodes.add(createINC(INC_Absolute, createAbsoluteRMWCycles(W65C816::INC)));
    opCodes.add(createSBC(SBC_AbsoluteLong, createAbsoluteLongCycles(W65C816::SBC)));
    opCodes.add(createBEQ(BEQ_ProgramCounterRelative, createRelativeShortCycles(W65C816::BEQ)));
    opCodes.add(createSBC(SBC_DirectPageIndirectIndexedWithY, createDirectIndirectIndexedWithYCycles(W65C816::SBC)));
    opCodes.add(createSBC(SBC_DirectPageIndirect, createDirectIndirectCycles(W65C816::SBC)));
    opCodes.add(createSBC(SBC_StackRelativeIndirectIndexedWithY, createStackRelativeIndirectIndexedWithYCycles(W65C816::SBC)));
    opCodes.add(createPEA(PEA_StackImmediate, createStackPEACycles()));
    opCodes.add(createSBC(SBC_DirectPageIndexedWithX, createDirectIndexedWithXCycles(W65C816::SBC, M)));
    opCodes.add(createINC(INC_DirectPageIndexedWithX, createDirectIndexedWithXRMWCycles(W65C816::INC)));
    opCodes.add(createSBC(SBC_DirectPageIndirectLongIndexedWithY, createDirectIndirectLongIndexedWithYCycles(W65C816::SBC)));
    opCodes.add(createSED(SED_Implied, createImpliedCycles(W65C816::SED)));
    opCodes.add(createSBC(SBC_AbsoluteIndexedWithY, createAbsoluteIndexedWithYCycles(W65C816::SBC, M)));
    opCodes.add(createPLX(PLX_StackImplied, createStackPullCycles(W65C816::PLX, XY)));
    opCodes.add(createXCE(XCE_Implied, createImpliedCycles(W65C816::XCE)));
    opCodes.add(createJSR(JSR_AbsoluteIndexedIndirectWithX, createAbsoluteIndexedIndirectWithXJSRCycles()));
    opCodes.add(createSBC(SBC_AbsoluteIndexedWithX, createAbsoluteIndexedWithXCycles(W65C816::SBC, M)));
    opCodes.add(createINC(INC_AbsoluteIndexedWithX, createAbsoluteIndexedWithXRMWCycles(W65C816::INC)));
    opCodes.add(createSBC(SBC_AbsoluteLongIndexedWithX, createAbsoluteLongIndexedWithXCycles(W65C816::SBC)));

    for (int i = 0; i <= 255; i++)
    {
      Instruction opCode = opCodes.get(i);
      if (opCode.getCode() != i)
      {
        throw new SimulatorException("OpCode [" + opCode.getName() + "] has code [" + opCode.getCode() + "] but is at index [" + i + "].");
      }
    }

    return opCodes.toArray(new Instruction[0]);
  }

  private static Instruction createBEQ(int code, InstructionCycles instructionCycles)
  {
    return new Instruction(code, instructionCycles, "BEQ",
                           "Branch if Equal (Z=1)");
  }

  private static Instruction createXBA(int code, InstructionCycles instructionCycles)
  {
    return new Instruction(code, instructionCycles, "XBA",
                           "Exchange B and A Accumulator");
  }

  private static Instruction createNOP(int code, InstructionCycles instructionCycles)
  {
    return new Instruction(code, instructionCycles, "NOP",
                           "No Operation for two cycles.");
  }

  private static Instruction createINX(int code, InstructionCycles instructionCycles)
  {
    return new Instruction(code, instructionCycles, "INX",
                           "Increment Index X by One");
  }

  private static Instruction createSEP(int code, InstructionCycles instructionCycles)
  {
    return new Instruction(code, instructionCycles, "SEP",
                           "Set Processor Status Bit");
  }

  private static Instruction createBNE(int code, InstructionCycles instructionCycles)
  {
    return new Instruction(code, instructionCycles, "BNE",
                           "Branch if Not Equal (Z=0)");
  }

  private static Instruction createWAI(int code, InstructionCycles instructionCycles)
  {
    return new Instruction(code, instructionCycles, "WAI",
                           "Wait for Interrupt");
  }

  private static Instruction createDEX(int code, InstructionCycles instructionCycles)
  {
    return new Instruction(code, instructionCycles, "DEX",
                           "Decrement Index X by One.");
  }

  private static Instruction createINY(int code, InstructionCycles instructionCycles)
  {
    return new Instruction(code, instructionCycles, "INY",
                           "Increment Index Y by One");
  }

  private static Instruction createPEA(int code, InstructionCycles instructionCycles)
  {
    return new Instruction(code, instructionCycles, "PEA",
                           "Push Absolute Address");
  }

  private static Instruction createINC(int code, InstructionCycles instructionCycles)
  {
    return new Instruction(code, instructionCycles, "INC",
                           "Increment memory; result in memory and update NZ.");
  }

  private static Instruction createXCE(int code, InstructionCycles instructionCycles)
  {
    return new Instruction(code, instructionCycles, "XCE",
                           "Exchange Carry and Emulation Bits");
  }

  private static Instruction createPLX(int code, InstructionCycles instructionCycles)
  {
    return new Instruction(code, instructionCycles, "PLX",
                           "Pull Index X from Stack");
  }

  private static Instruction createSED(int code, InstructionCycles instructionCycles)
  {
    return new Instruction(code, instructionCycles,
                           "SED", "Set Decimal Mode");
  }

  private static Instruction createSBC(int code, InstructionCycles instructionCycles)
  {
    return new Instruction(code, instructionCycles, "SBC",
                           "Subtract memory and carry from A; result in A and update NZC.");
  }

  private static Instruction createCPX(int code, InstructionCycles instructionCycles)
  {
    return new Instruction(code, instructionCycles, "CPX",
                           "Compare Memory and Index X");
  }

  private static Instruction createSTP(int code, InstructionCycles instructionCycles)
  {
    return new Instruction(code, instructionCycles, "STP",
                           "Stop the Clock");
  }

  private static Instruction createPHX(int code, InstructionCycles instructionCycles)
  {
    return new Instruction(code, instructionCycles, "PHX",
                           "Push Index X on Stack");
  }

  private static Instruction createCLD(int code, InstructionCycles instructionCycles)
  {
    return new Instruction(code, instructionCycles, "CLD",
                           "Clear Decimal Mode");
  }

  private static Instruction createPEI(int code, InstructionCycles instructionCycles)
  {
    return new Instruction(code, instructionCycles, "PEI",
                           "Push Indirect Address");
  }

  private static Instruction createDEC(int code, InstructionCycles instructionCycles)
  {
    return new Instruction(code, instructionCycles, "DEC",
                           "Decrement memory; result in memory and update NZ.");
  }

  private static Instruction createREP(int code, InstructionCycles instructionCycles)
  {
    return new Instruction(code, instructionCycles, "REP",
                           "Reset Status Bits");
  }

  private static Instruction createCMP(int code, InstructionCycles instructionCycles)
  {
    return new Instruction(code, instructionCycles, "CMP",
                           "Compare Memory and Accumulator");
  }

  private static Instruction createCPY(int code, InstructionCycles instructionCycles)
  {
    return new Instruction(code, instructionCycles, "CPY",
                           "Compare Memory and Index Y");
  }

  private static Instruction createTYX(int code, InstructionCycles instructionCycles)
  {
    return new Instruction(code, instructionCycles, "TYX",
                           "Transfer Index Y to Index X");
  }

  private static Instruction createTSX(int code, InstructionCycles instructionCycles)
  {
    return new Instruction(code, instructionCycles, "TSX",
                           "Transfer Stack Pointer Register to Index X");
  }

  private static Instruction createCLV(int code, InstructionCycles instructionCycles)
  {
    return new Instruction(code, instructionCycles, "CLV",
                           "Clear Overflow Flag");
  }

  private static Instruction createBCS(int code, InstructionCycles instructionCycles)
  {
    return new Instruction(code, instructionCycles, "BCS",
                           "Branch on Carry Set (C=1)");
  }

  private static Instruction createPLB(int code, InstructionCycles instructionCycles)
  {
    return new Instruction(code, instructionCycles, "PLB",
                           "Pull Data Bank Register from Stack");
  }

  private static Instruction createTAX(int code, InstructionCycles instructionCycles)
  {
    return new Instruction(code, instructionCycles, "TAX",
                           "Transfer Accumulator in Index X");
  }

  private static Instruction createTAY(int code, InstructionCycles instructionCycles)
  {
    return new Instruction(code, instructionCycles, "TAY",
                           "Transfer Accumulator in Index Y");
  }

  private static Instruction createLDX(int code, InstructionCycles instructionCycles)
  {
    return new Instruction(code, instructionCycles, "LDX", "Load Index X with Memory");
  }

  private static Instruction createBRL(int code, InstructionCycles instructionCycles)
  {
    return new Instruction(code, instructionCycles, "BRL",
                           "Branch Always Long");
  }

  private static Instruction createBRA(int code, InstructionCycles instructionCycles)
  {
    return new Instruction(code, instructionCycles, "BRA",
                           "Branch Always");
  }

  private static Instruction createTDC(int code, InstructionCycles instructionCycles)
  {
    return new Instruction(code, instructionCycles, "TDC",
                           "Transfer Direct Register to C Accumulator");
  }

  private static Instruction createPLY(int code, InstructionCycles instructionCycles)
  {
    return new Instruction(code, instructionCycles, "PLY",
                           "Pull Index Y from Stack");
  }

  private static Instruction createSEI(int code, InstructionCycles instructionCycles)
  {
    return new Instruction(code, instructionCycles, "SEI",
                           "Set Interrupt Disable Status");
  }

  private static Instruction createBVS(int code, InstructionCycles instructionCycles)
  {
    return new Instruction(code, instructionCycles, "BVS", "Branch on Overflow Set (V=1)");
  }

  private static Instruction createRTL(int code, InstructionCycles instructionCycles)
  {
    return new Instruction(code, instructionCycles, "RTL",
                           "Return from Subroutine Long");
  }

  private static Instruction createRRA(int code, InstructionCycles instructionCycles)
  {
    return new Instruction(code, instructionCycles, "ROR",
                           "Rotate accumulator right one bit; update NZC.");
  }

  private static Instruction createPLA(int code, InstructionCycles instructionCycles)
  {
    return new Instruction(code, instructionCycles, "PLA",
                           "Pull Accumulator from Stack");
  }

  private static Instruction createROR(int code, InstructionCycles instructionCycles)
  {
    return new Instruction(code, instructionCycles, "ROR",
                           "Rotate memory right one bit; update NZC.");
  }

  private static Instruction createPER(int code, InstructionCycles instructionCycles)
  {
    return new Instruction(code, instructionCycles, "PER",
                           "Push Program Counter Relative Address");
  }

  private static Instruction createADC(int code, InstructionCycles instructionCycles)
  {
    return new Instruction(code, instructionCycles, "ADC",
                           "Add memory and carry to A; result in A and update NZC.");
  }

  private static Instruction createRTS(int code, InstructionCycles instructionCycles)
  {
    return new Instruction(code, instructionCycles, "RTS",
                           "Return from Subroutine");
  }

  private static Instruction createTCD(int code, InstructionCycles instructionCycles)
  {
    return new Instruction(code, instructionCycles, "TCD",
                           "Transfer C Accumulator to Direct Register");
  }

  private static Instruction createPHY(int code, InstructionCycles instructionCycles)
  {
    return new Instruction(code, instructionCycles, "PHY",
                           "Push Index Y on Stack");
  }

  private static Instruction createCLI(int code, InstructionCycles instructionCycles)
  {
    return new Instruction(code, instructionCycles, "CLI",
                           "Clear Interrupt Disable Bit");
  }

  private static Instruction createMVN(int code, InstructionCycles instructionCycles)
  {
    return new Instruction(code, instructionCycles, "MVN",
                           "Block move next...");
  }

  private static Instruction createBVC(int code, InstructionCycles instructionCycles)
  {
    return new Instruction(code, instructionCycles, "BVC",
                           "Branch on Overflow Clear (V=0)");
  }

  private static Instruction createJMP(int code, InstructionCycles instructionCycles)
  {
    return new Instruction(code, instructionCycles, "JMP",
                           "Jump to New Location");
  }

  private static Instruction createPHK(int code, InstructionCycles instructionCycles)
  {
    return new Instruction(code, instructionCycles, "PHK",
                           "Push Program Bank Register on Stack");
  }

  private static Instruction createSRA(int code, InstructionCycles instructionCycles)
  {
    return new Instruction(code, instructionCycles, "LSR",
                           "Shift accumulator right one bit; update NZC.");
  }

  private static Instruction createPHA(int code, InstructionCycles instructionCycles)
  {
    return new Instruction(code, instructionCycles, "PHA",
                           "Push Accumulator onto Stack");
  }

  private static Instruction createLSR(int code_lsr, InstructionCycles instructionCycles)
  {
    return new Instruction(code_lsr, instructionCycles,
                           "LSR", "Shift memory right one bit; update NZC.");
  }

  private static Instruction createMVP(int code, InstructionCycles instructionCycles)
  {
    return new Instruction(code, instructionCycles, "MVP",
                           "Block move previous...");
  }

  private static Instruction createWDM(int code, InstructionCycles instructionCycles)
  {
    return new Instruction(code, instructionCycles, "WDM",
                           "Reserved for future use");
  }

  private static Instruction createEOR(int code, InstructionCycles instructionCycles)
  {
    return new Instruction(code, instructionCycles, "EOR",
                           "'Exclusive OR' Memory with Accumulator");
  }

  private static Instruction createRTI(int code, InstructionCycles instructionCycles)
  {
    return new Instruction(code, instructionCycles, "RTI",
                           "Return from Interrupt");
  }

  private static Instruction createTSC(int code, InstructionCycles instructionCycles)
  {
    return new Instruction(code, instructionCycles, "TSC",
                           "Transfer Stack Pointer to C Accumulator");
  }

  private static Instruction createDEA(int code, InstructionCycles instructionCycles)
  {
    return new Instruction(code, instructionCycles, "DEC",
                           "Decrement accumulator; update NZ.");
  }

  private static Instruction createSEC(int code, InstructionCycles instructionCycles)
  {
    return new Instruction(code, instructionCycles, "SEC",
                           "Set Carry Flag");
  }

  private static Instruction createBMI(int code, InstructionCycles instructionCycles)
  {
    return new Instruction(code, instructionCycles, "BMI",
                           "Branch if Result Minus (N=1)");
  }

  private static Instruction createPLP(int code, InstructionCycles instructionCycles)
  {
    return new Instruction(code, instructionCycles, "PLP",
                           "Pull Processor Status from Stack");
  }

  private static Instruction createRLA(int code, InstructionCycles instructionCycles)
  {
    return new Instruction(code, instructionCycles, "ROL",
                           "Rotate Accumulator One Bit Left.");
  }

  private static Instruction createPLD(int code, InstructionCycles instructionCycles)
  {
    return new Instruction(code, instructionCycles, "PLD",
                           "Pull Direct Register from Stack");
  }

  private static Instruction createROL(int code, InstructionCycles instructionCycles)
  {
    return new Instruction(code, instructionCycles, "ROL",
                           "Rotate Memory One Bit Left.");
  }

  private static Instruction createLDA(int code, InstructionCycles instructionCycles)
  {
    return new Instruction(code, instructionCycles, "LDA",
                           "Load Accumulator with Memory");
  }

  private static Instruction createLDY(int code, InstructionCycles immediateCycles)
  {
    return new Instruction(code, immediateCycles, "LDY",
                           "Load Index Y with Memory");
  }

  private static Instruction createPHB(int code, InstructionCycles instructionCycles)
  {
    return new Instruction(code, instructionCycles, "PHB",
                           "Push Data Bank Register on Stack");
  }

  private static Instruction createTXA(int code, InstructionCycles instructionCycles)
  {
    return new Instruction(code, instructionCycles, "TXA",
                           "Transfer Index X to Accumulator");
  }

  private static Instruction createSTY(int code, InstructionCycles instructionCycles)
  {
    return new Instruction(code, instructionCycles, "STY",
                           "Store Index Y in Memory");
  }

  private static Instruction createBCC(int code, InstructionCycles instructionCycles)
  {
    return new Instruction(code, instructionCycles, "BCC",
                           "Branch on Carry Clear (C=0)");
  }

  private static Instruction createSTX(int code, InstructionCycles instructionCycles)
  {
    return new Instruction(code, instructionCycles, "STX",
                           "Store Index X in Memory");
  }

  private static Instruction createTYA(int code, InstructionCycles instructionCycles)
  {
    return new Instruction(code, instructionCycles, "TYA",
                           "Transfer Index Y to Accumulator");
  }

  private static Instruction createTXS(int code, InstructionCycles instructionCycles)
  {
    return new Instruction(code, instructionCycles, "TXS",
                           "Transfer Index X to Stack Pointer Register");
  }

  private static Instruction createTXY(int code, InstructionCycles instructionCycles)
  {
    return new Instruction(code, instructionCycles, "TXY",
                           "Transfer Index X to Index Y");
  }

  private static Instruction createSTZ(int stz_absolute, InstructionCycles absoluteWriteCycles)
  {
    return new Instruction(stz_absolute, absoluteWriteCycles,
                           "STZ", "Store Zero in Memory");
  }

  private static Instruction createSTA(int code, InstructionCycles instructionCycles)
  {
    return new Instruction(code, instructionCycles, "STA",
                           "Store Accumulator in Memory");
  }

  private static Instruction createDEY(int code, InstructionCycles instructionCycles)
  {
    return new Instruction(code, instructionCycles, "DEY", "Decrement Index Y by One");
  }

  private static Instruction createBIT(int code, InstructionCycles instructionCycles)
  {
    return new Instruction(code, instructionCycles, "BIT",
                           "Bit Test");
  }

  private static Instruction createJSL(int code, InstructionCycles instructionCycles)
  {
    return new Instruction(code, instructionCycles, "JSL",
                           "Jump long to new location save return address on Stack.");
  }

  private static Instruction createAND(int code, InstructionCycles instructionCycles)
  {
    return new Instruction(code, instructionCycles, "AND",
                           "Bitwise AND memory with A; result in A and update NZ.");
  }

  private static Instruction createJSR(int code, InstructionCycles instructionCycles)
  {
    return new Instruction(code, instructionCycles, "JSR",
                           "Jump to new location save return address on Stack.");
  }

  private static Instruction createTCS(int code, InstructionCycles instructionCycles)
  {
    return new Instruction(code, instructionCycles, "TCS",
                           "Transfer C Accumulator to Stack Pointer");
  }

  private static Instruction createINA(int code, InstructionCycles busCycles)
  {
    return new Instruction(code, busCycles, "INC",
                           "Increment accumulator; update NZ.");
  }

  private static Instruction createCLC(int code, InstructionCycles instructionCycles)
  {
    return new Instruction(code, instructionCycles, "CLC",
                           "Clear Carry Flag");
  }

  private static Instruction createTRB(int code, InstructionCycles instructionCycles)
  {
    return new Instruction(code, instructionCycles, "TRB",
                           "Test and Reset Bit");
  }

  private static Instruction createBPL(int code, InstructionCycles instructionCycles)
  {
    return new Instruction(code, instructionCycles, "BPL",
                           "Branch if Result Plus (N=0)");
  }

  private static Instruction createPHD(int code, InstructionCycles instructionCycles)
  {
    return new Instruction(code, instructionCycles, "PHD",
                           "Push Direct Register on Stack");
  }

  private static Instruction createSLA(int code, InstructionCycles instructionCycles)
  {
    return new Instruction(code, instructionCycles, "ASL",
                           "Shift accumulator left one bit; update NZC.");
  }

  private static Instruction createASL(int code, InstructionCycles directRMWCycles)
  {
    return new Instruction(code, directRMWCycles, "ASL", "Shift memory left 1 bit; result in memory and update NZC.");
  }

  private static Instruction createPHP(int code, InstructionCycles instructionCycles)
  {
    return new Instruction(code, instructionCycles, "PHP", "Push Processor Status on Stack");
  }

  private static Instruction createTSB(int code, InstructionCycles instructionCycles)
  {
    return new Instruction(code, instructionCycles,
                           "TSB", "Test and Set Bit");
  }

  private static Instruction createCOP(int code, InstructionCycles instructionCycles)
  {
    return new Instruction(code, instructionCycles, "COP",
                           "Force co-processor software interrupt.");
  }

  private static Instruction createORA(int code, InstructionCycles directIndexedIndirectWithXCycles)
  {
    return new Instruction(code, directIndexedIndirectWithXCycles,
                           "ORA", "'OR' memory with A; result in A and update NZ.");
  }

  private static Instruction createBrk(int code, InstructionCycles instructionCycles)
  {
    return new Instruction(code, instructionCycles, "BRK",
                           "Force break software interrupt.");
  }

  public static Instruction createReset()
  {
    return new Instruction(-1, createStackResetCycles(new ResetVector(), W65C816::RES), "RES",
                           "Reset the CPU.");
  }

  public static Instruction createIRQ()
  {
    return new Instruction(-1, createStackHardwareInterruptCycles(new IRQVector(), W65C816::IRQ), "IRQ",
                           "Interrupt request.");
  }

  public static Instruction createNMI()
  {
    return new Instruction(-1, createStackHardwareInterruptCycles(new NMIVector(), W65C816::NMI), "NMI",
                           "Non-maskable interrupt.");
  }

  public static Instruction createAbort()
  {
    return new Instruction(-1, createStackAbortInterruptCycles(new AbortVector(), W65C816::ABORT), "ABORT",
                           "Stop the current instruction and return processor status to what it was prior to the current instruction.");
  }

  public static Instruction createFetchNext()
  {
    return new Instruction(-1, createFetchOpCodeCycles(), "NEXT",
                           "Fetch Opcode from address in program counter.");
  }
}

