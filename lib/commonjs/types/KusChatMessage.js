"use strict";

Object.defineProperty(exports, "__esModule", {
  value: true
});
exports.KustomerMessageTemplateType = exports.KustomerDirectionType = void 0;
let KustomerMessageTemplateType = /*#__PURE__*/function (KustomerMessageTemplateType) {
  KustomerMessageTemplateType["quickReplies"] = "quick_replies";
  KustomerMessageTemplateType["mll"] = "mll";
  KustomerMessageTemplateType["deflection"] = "deflection";
  KustomerMessageTemplateType["text"] = "text";
  KustomerMessageTemplateType["list"] = "list";
  KustomerMessageTemplateType["feedback"] = "answer_button_feedback";
  KustomerMessageTemplateType["none"] = "";
  return KustomerMessageTemplateType;
}({});
exports.KustomerMessageTemplateType = KustomerMessageTemplateType;
let KustomerDirectionType = /*#__PURE__*/function (KustomerDirectionType) {
  KustomerDirectionType["initialIn"] = "initial-in";
  KustomerDirectionType["initialOut"] = "initial-out";
  KustomerDirectionType["followupOut"] = "followup-out";
  return KustomerDirectionType;
}({});
exports.KustomerDirectionType = KustomerDirectionType;
//# sourceMappingURL=KusChatMessage.js.map