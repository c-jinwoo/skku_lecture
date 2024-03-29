
// Generated from Expr.g4 by ANTLR 4.11.1


#include "ExprLexer.h"


using namespace antlr4;



using namespace antlr4;

namespace {

struct ExprLexerStaticData final {
  ExprLexerStaticData(std::vector<std::string> ruleNames,
                          std::vector<std::string> channelNames,
                          std::vector<std::string> modeNames,
                          std::vector<std::string> literalNames,
                          std::vector<std::string> symbolicNames)
      : ruleNames(std::move(ruleNames)), channelNames(std::move(channelNames)),
        modeNames(std::move(modeNames)), literalNames(std::move(literalNames)),
        symbolicNames(std::move(symbolicNames)),
        vocabulary(this->literalNames, this->symbolicNames) {}

  ExprLexerStaticData(const ExprLexerStaticData&) = delete;
  ExprLexerStaticData(ExprLexerStaticData&&) = delete;
  ExprLexerStaticData& operator=(const ExprLexerStaticData&) = delete;
  ExprLexerStaticData& operator=(ExprLexerStaticData&&) = delete;

  std::vector<antlr4::dfa::DFA> decisionToDFA;
  antlr4::atn::PredictionContextCache sharedContextCache;
  const std::vector<std::string> ruleNames;
  const std::vector<std::string> channelNames;
  const std::vector<std::string> modeNames;
  const std::vector<std::string> literalNames;
  const std::vector<std::string> symbolicNames;
  const antlr4::dfa::Vocabulary vocabulary;
  antlr4::atn::SerializedATNView serializedATN;
  std::unique_ptr<antlr4::atn::ATN> atn;
};

::antlr4::internal::OnceFlag exprlexerLexerOnceFlag;
ExprLexerStaticData *exprlexerLexerStaticData = nullptr;

void exprlexerLexerInitialize() {
  assert(exprlexerLexerStaticData == nullptr);
  auto staticData = std::make_unique<ExprLexerStaticData>(
    std::vector<std::string>{
      "T__0", "T__1", "T__2", "T__3", "T__4", "T__5", "T__6", "T__7", "NEWLINE", 
      "INT", "FLOAT", "ID", "WS"
    },
    std::vector<std::string>{
      "DEFAULT_TOKEN_CHANNEL", "HIDDEN"
    },
    std::vector<std::string>{
      "DEFAULT_MODE"
    },
    std::vector<std::string>{
      "", "';'", "'*'", "'/'", "'+'", "'-'", "'('", "')'", "'='"
    },
    std::vector<std::string>{
      "", "", "", "", "", "", "", "", "", "NEWLINE", "INT", "FLOAT", "ID", 
      "WS"
    }
  );
  static const int32_t serializedATNSegment[] = {
  	4,0,13,85,6,-1,2,0,7,0,2,1,7,1,2,2,7,2,2,3,7,3,2,4,7,4,2,5,7,5,2,6,7,
  	6,2,7,7,7,2,8,7,8,2,9,7,9,2,10,7,10,2,11,7,11,2,12,7,12,1,0,1,0,1,1,1,
  	1,1,2,1,2,1,3,1,3,1,4,1,4,1,5,1,5,1,6,1,6,1,7,1,7,1,8,4,8,45,8,8,11,8,
  	12,8,46,1,9,3,9,50,8,9,1,9,4,9,53,8,9,11,9,12,9,54,1,10,3,10,58,8,10,
  	1,10,4,10,61,8,10,11,10,12,10,62,1,10,1,10,5,10,67,8,10,10,10,12,10,70,
  	9,10,1,11,1,11,5,11,74,8,11,10,11,12,11,77,9,11,1,12,4,12,80,8,12,11,
  	12,12,12,81,1,12,1,12,0,0,13,1,1,3,2,5,3,7,4,9,5,11,6,13,7,15,8,17,9,
  	19,10,21,11,23,12,25,13,1,0,6,2,0,10,10,13,13,2,0,43,43,45,45,1,0,48,
  	57,3,0,65,90,95,95,97,122,4,0,48,57,65,90,95,95,97,122,3,0,9,10,13,13,
  	32,32,92,0,1,1,0,0,0,0,3,1,0,0,0,0,5,1,0,0,0,0,7,1,0,0,0,0,9,1,0,0,0,
  	0,11,1,0,0,0,0,13,1,0,0,0,0,15,1,0,0,0,0,17,1,0,0,0,0,19,1,0,0,0,0,21,
  	1,0,0,0,0,23,1,0,0,0,0,25,1,0,0,0,1,27,1,0,0,0,3,29,1,0,0,0,5,31,1,0,
  	0,0,7,33,1,0,0,0,9,35,1,0,0,0,11,37,1,0,0,0,13,39,1,0,0,0,15,41,1,0,0,
  	0,17,44,1,0,0,0,19,49,1,0,0,0,21,57,1,0,0,0,23,71,1,0,0,0,25,79,1,0,0,
  	0,27,28,5,59,0,0,28,2,1,0,0,0,29,30,5,42,0,0,30,4,1,0,0,0,31,32,5,47,
  	0,0,32,6,1,0,0,0,33,34,5,43,0,0,34,8,1,0,0,0,35,36,5,45,0,0,36,10,1,0,
  	0,0,37,38,5,40,0,0,38,12,1,0,0,0,39,40,5,41,0,0,40,14,1,0,0,0,41,42,5,
  	61,0,0,42,16,1,0,0,0,43,45,7,0,0,0,44,43,1,0,0,0,45,46,1,0,0,0,46,44,
  	1,0,0,0,46,47,1,0,0,0,47,18,1,0,0,0,48,50,7,1,0,0,49,48,1,0,0,0,49,50,
  	1,0,0,0,50,52,1,0,0,0,51,53,7,2,0,0,52,51,1,0,0,0,53,54,1,0,0,0,54,52,
  	1,0,0,0,54,55,1,0,0,0,55,20,1,0,0,0,56,58,7,1,0,0,57,56,1,0,0,0,57,58,
  	1,0,0,0,58,60,1,0,0,0,59,61,7,2,0,0,60,59,1,0,0,0,61,62,1,0,0,0,62,60,
  	1,0,0,0,62,63,1,0,0,0,63,64,1,0,0,0,64,68,5,46,0,0,65,67,7,2,0,0,66,65,
  	1,0,0,0,67,70,1,0,0,0,68,66,1,0,0,0,68,69,1,0,0,0,69,22,1,0,0,0,70,68,
  	1,0,0,0,71,75,7,3,0,0,72,74,7,4,0,0,73,72,1,0,0,0,74,77,1,0,0,0,75,73,
  	1,0,0,0,75,76,1,0,0,0,76,24,1,0,0,0,77,75,1,0,0,0,78,80,7,5,0,0,79,78,
  	1,0,0,0,80,81,1,0,0,0,81,79,1,0,0,0,81,82,1,0,0,0,82,83,1,0,0,0,83,84,
  	6,12,0,0,84,26,1,0,0,0,9,0,46,49,54,57,62,68,75,81,1,6,0,0
  };
  staticData->serializedATN = antlr4::atn::SerializedATNView(serializedATNSegment, sizeof(serializedATNSegment) / sizeof(serializedATNSegment[0]));

  antlr4::atn::ATNDeserializer deserializer;
  staticData->atn = deserializer.deserialize(staticData->serializedATN);

  const size_t count = staticData->atn->getNumberOfDecisions();
  staticData->decisionToDFA.reserve(count);
  for (size_t i = 0; i < count; i++) { 
    staticData->decisionToDFA.emplace_back(staticData->atn->getDecisionState(i), i);
  }
  exprlexerLexerStaticData = staticData.release();
}

}

ExprLexer::ExprLexer(CharStream *input) : Lexer(input) {
  ExprLexer::initialize();
  _interpreter = new atn::LexerATNSimulator(this, *exprlexerLexerStaticData->atn, exprlexerLexerStaticData->decisionToDFA, exprlexerLexerStaticData->sharedContextCache);
}

ExprLexer::~ExprLexer() {
  delete _interpreter;
}

std::string ExprLexer::getGrammarFileName() const {
  return "Expr.g4";
}

const std::vector<std::string>& ExprLexer::getRuleNames() const {
  return exprlexerLexerStaticData->ruleNames;
}

const std::vector<std::string>& ExprLexer::getChannelNames() const {
  return exprlexerLexerStaticData->channelNames;
}

const std::vector<std::string>& ExprLexer::getModeNames() const {
  return exprlexerLexerStaticData->modeNames;
}

const dfa::Vocabulary& ExprLexer::getVocabulary() const {
  return exprlexerLexerStaticData->vocabulary;
}

antlr4::atn::SerializedATNView ExprLexer::getSerializedATN() const {
  return exprlexerLexerStaticData->serializedATN;
}

const atn::ATN& ExprLexer::getATN() const {
  return *exprlexerLexerStaticData->atn;
}




void ExprLexer::initialize() {
  ::antlr4::internal::call_once(exprlexerLexerOnceFlag, exprlexerLexerInitialize);
}
