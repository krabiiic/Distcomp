var __esDecorate = (this && this.__esDecorate) || function (ctor, descriptorIn, decorators, contextIn, initializers, extraInitializers) {
    function accept(f) { if (f !== void 0 && typeof f !== "function") throw new TypeError("Function expected"); return f; }
    var kind = contextIn.kind, key = kind === "getter" ? "get" : kind === "setter" ? "set" : "value";
    var target = !descriptorIn && ctor ? contextIn["static"] ? ctor : ctor.prototype : null;
    var descriptor = descriptorIn || (target ? Object.getOwnPropertyDescriptor(target, contextIn.name) : {});
    var _, done = false;
    for (var i = decorators.length - 1; i >= 0; i--) {
        var context = {};
        for (var p in contextIn) context[p] = p === "access" ? {} : contextIn[p];
        for (var p in contextIn.access) context.access[p] = contextIn.access[p];
        context.addInitializer = function (f) { if (done) throw new TypeError("Cannot add initializers after decoration has completed"); extraInitializers.push(accept(f || null)); };
        var result = (0, decorators[i])(kind === "accessor" ? { get: descriptor.get, set: descriptor.set } : descriptor[key], context);
        if (kind === "accessor") {
            if (result === void 0) continue;
            if (result === null || typeof result !== "object") throw new TypeError("Object expected");
            if (_ = accept(result.get)) descriptor.get = _;
            if (_ = accept(result.set)) descriptor.set = _;
            if (_ = accept(result.init)) initializers.unshift(_);
        }
        else if (_ = accept(result)) {
            if (kind === "field") initializers.unshift(_);
            else descriptor[key] = _;
        }
    }
    if (target) Object.defineProperty(target, contextIn.name, descriptor);
    done = true;
};
var __runInitializers = (this && this.__runInitializers) || function (thisArg, initializers, value) {
    var useValue = arguments.length > 2;
    for (var i = 0; i < initializers.length; i++) {
        value = useValue ? initializers[i].call(thisArg, value) : initializers[i].call(thisArg);
    }
    return useValue ? value : void 0;
};
var __setFunctionName = (this && this.__setFunctionName) || function (f, name, prefix) {
    if (typeof name === "symbol") name = name.description ? "[".concat(name.description, "]") : "";
    return Object.defineProperty(f, "name", { configurable: true, value: prefix ? "".concat(prefix, " ", name) : name });
};
import { PrimaryGeneratedColumn, Column, ManyToOne, OneToMany, ManyToMany, JoinTable, Entity, } from 'typeorm';
import { User } from './user.entity';
import { Message } from './message.entity';
import { Mark } from './mark.entity';
let Article = (() => {
    let _classDecorators = [Entity('tbl_article')];
    let _classDescriptor;
    let _classExtraInitializers = [];
    let _classThis;
    let _id_decorators;
    let _id_initializers = [];
    let _id_extraInitializers = [];
    let _title_decorators;
    let _title_initializers = [];
    let _title_extraInitializers = [];
    let _content_decorators;
    let _content_initializers = [];
    let _content_extraInitializers = [];
    let _created_decorators;
    let _created_initializers = [];
    let _created_extraInitializers = [];
    let _modified_decorators;
    let _modified_initializers = [];
    let _modified_extraInitializers = [];
    let _user_decorators;
    let _user_initializers = [];
    let _user_extraInitializers = [];
    let _messages_decorators;
    let _messages_initializers = [];
    let _messages_extraInitializers = [];
    let _marks_decorators;
    let _marks_initializers = [];
    let _marks_extraInitializers = [];
    var Article = _classThis = class {
        constructor() {
            this.id = __runInitializers(this, _id_initializers, void 0);
            this.title = (__runInitializers(this, _id_extraInitializers), __runInitializers(this, _title_initializers, void 0));
            this.content = (__runInitializers(this, _title_extraInitializers), __runInitializers(this, _content_initializers, void 0));
            this.created = (__runInitializers(this, _content_extraInitializers), __runInitializers(this, _created_initializers, void 0));
            this.modified = (__runInitializers(this, _created_extraInitializers), __runInitializers(this, _modified_initializers, void 0));
            this.user = (__runInitializers(this, _modified_extraInitializers), __runInitializers(this, _user_initializers, void 0));
            this.messages = (__runInitializers(this, _user_extraInitializers), __runInitializers(this, _messages_initializers, void 0));
            this.marks = (__runInitializers(this, _messages_extraInitializers), __runInitializers(this, _marks_initializers, void 0));
            __runInitializers(this, _marks_extraInitializers);
        }
    };
    __setFunctionName(_classThis, "Article");
    (() => {
        const _metadata = typeof Symbol === "function" && Symbol.metadata ? Object.create(null) : void 0;
        _id_decorators = [PrimaryGeneratedColumn()];
        _title_decorators = [Column({ length: 64 })];
        _content_decorators = [Column()];
        _created_decorators = [Column()];
        _modified_decorators = [Column()];
        _user_decorators = [ManyToOne(() => User, (user) => user.articles)];
        _messages_decorators = [OneToMany(() => Message, (message) => message.article)];
        _marks_decorators = [ManyToMany(() => Mark), JoinTable({ name: 'tbl_article_mark' })];
        __esDecorate(null, null, _id_decorators, { kind: "field", name: "id", static: false, private: false, access: { has: obj => "id" in obj, get: obj => obj.id, set: (obj, value) => { obj.id = value; } }, metadata: _metadata }, _id_initializers, _id_extraInitializers);
        __esDecorate(null, null, _title_decorators, { kind: "field", name: "title", static: false, private: false, access: { has: obj => "title" in obj, get: obj => obj.title, set: (obj, value) => { obj.title = value; } }, metadata: _metadata }, _title_initializers, _title_extraInitializers);
        __esDecorate(null, null, _content_decorators, { kind: "field", name: "content", static: false, private: false, access: { has: obj => "content" in obj, get: obj => obj.content, set: (obj, value) => { obj.content = value; } }, metadata: _metadata }, _content_initializers, _content_extraInitializers);
        __esDecorate(null, null, _created_decorators, { kind: "field", name: "created", static: false, private: false, access: { has: obj => "created" in obj, get: obj => obj.created, set: (obj, value) => { obj.created = value; } }, metadata: _metadata }, _created_initializers, _created_extraInitializers);
        __esDecorate(null, null, _modified_decorators, { kind: "field", name: "modified", static: false, private: false, access: { has: obj => "modified" in obj, get: obj => obj.modified, set: (obj, value) => { obj.modified = value; } }, metadata: _metadata }, _modified_initializers, _modified_extraInitializers);
        __esDecorate(null, null, _user_decorators, { kind: "field", name: "user", static: false, private: false, access: { has: obj => "user" in obj, get: obj => obj.user, set: (obj, value) => { obj.user = value; } }, metadata: _metadata }, _user_initializers, _user_extraInitializers);
        __esDecorate(null, null, _messages_decorators, { kind: "field", name: "messages", static: false, private: false, access: { has: obj => "messages" in obj, get: obj => obj.messages, set: (obj, value) => { obj.messages = value; } }, metadata: _metadata }, _messages_initializers, _messages_extraInitializers);
        __esDecorate(null, null, _marks_decorators, { kind: "field", name: "marks", static: false, private: false, access: { has: obj => "marks" in obj, get: obj => obj.marks, set: (obj, value) => { obj.marks = value; } }, metadata: _metadata }, _marks_initializers, _marks_extraInitializers);
        __esDecorate(null, _classDescriptor = { value: _classThis }, _classDecorators, { kind: "class", name: _classThis.name, metadata: _metadata }, null, _classExtraInitializers);
        Article = _classThis = _classDescriptor.value;
        if (_metadata) Object.defineProperty(_classThis, Symbol.metadata, { enumerable: true, configurable: true, writable: true, value: _metadata });
        __runInitializers(_classThis, _classExtraInitializers);
    })();
    return Article = _classThis;
})();
export { Article };
