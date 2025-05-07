import { Test, TestingModule } from '@nestjs/testing';
import { EditorController } from './Editor.Controller';
import { EditorService } from './Editor.Service';
import { EditorRequestTo, UpdateEditorDto } from './Dto/EditorRequestTo';
import { EditorResponseTo } from './Dto/EditorResponseTo';

describe('EditorController', () => {
  let controller: EditorController;
  let mockEditorService: {
    getAllEditors: jest.Mock;
    findById: jest.Mock;
    createEditor: jest.Mock;
    deleteEditor: jest.Mock;
    updateEditor: jest.Mock;
    getEditorByArticleId: jest.Mock;
  };

  beforeEach(async () => {
    mockEditorService = {
      getAllEditors: jest.fn(),
      findById: jest.fn(),
      createEditor: jest.fn(),
      deleteEditor: jest.fn(),
      updateEditor: jest.fn(),
      getEditorByArticleId: jest.fn(),
    };

    const module: TestingModule = await Test.createTestingModule({
      controllers: [EditorController],
      providers: [
        {
          provide: EditorService,
          useValue: mockEditorService,
        },
      ],
    }).compile();

    controller = module.get<EditorController>(EditorController);
  });

  // Test findAll method
  describe('findAll', () => {
    it('should return an array of editors', async () => {
      const mockEditors: EditorResponseTo[] = [
        {
          id: 1,
          login: 'editor1',
          firstname: 'John',
          lastname: 'Doe',
        },
      ];
      mockEditorService.getAllEditors.mockResolvedValue(mockEditors);

      const result = await controller.findAll();
      expect(result).toEqual(mockEditors);
      expect(mockEditorService.getAllEditors).toHaveBeenCalled();
    });
  });

  // Test findById method
  describe('findById', () => {
    it('should return an editor by ID', async () => {
      const mockEditor: EditorResponseTo = {
        id: 1,
        login: 'editor1',
        firstname: 'John',
        lastname: 'Doe',
      };
      mockEditorService.findById.mockResolvedValue(mockEditor);

      const result = await controller.findById(1);
      expect(result).toEqual(mockEditor);
      expect(mockEditorService.findById).toHaveBeenCalledWith(1);
    });
  });

  // Test create method
  describe('create', () => {
    it('should create a new editor', async () => {
      const createEditorDto: EditorRequestTo = {
        login: 'neweditor',
        password: 'password123',
        firstname: 'Jane',
        lastname: 'Smith',
      };

      const createdEditor: EditorResponseTo = {
        id: 2,
        login: 'neweditor',
        firstname: 'Jane',
        lastname: 'Smith',
      };

      mockEditorService.createEditor.mockResolvedValue(createdEditor);

      const result = await controller.create(createEditorDto);
      expect(result).toEqual(createdEditor);
      expect(mockEditorService.createEditor).toHaveBeenCalledWith(
        createEditorDto,
      );
    });
  });

  // Test delete method
  describe('delete', () => {
    it('should delete an editor', async () => {
      mockEditorService.deleteEditor.mockResolvedValue(undefined);

      await controller.delete(1);
      expect(mockEditorService.deleteEditor).toHaveBeenCalledWith(1);
    });
  });

  // Test update method
  describe('update', () => {
    it('should update an editor', async () => {
      const updateEditorDto: UpdateEditorDto = {
        id: 1,
        login: 'updatedlogin',
        password: 'newpassword123',
        firstname: 'Updated',
        lastname: 'Name',
      };

      const updatedEditor: EditorResponseTo = {
        id: 1,
        login: 'updatedlogin',
        firstname: 'Updated',
        lastname: 'Name',
      };

      mockEditorService.updateEditor.mockResolvedValue(updatedEditor);

      const result = await controller.update(updateEditorDto);
      expect(result).toEqual(updatedEditor);
      expect(mockEditorService.updateEditor).toHaveBeenCalledWith(
        updateEditorDto,
      );
    });
  });

  // Test getByArticleId method
  describe('getByArticleId', () => {
    it('should return an editor by article ID', async () => {
      const mockEditor: EditorResponseTo = {
        id: 1,
        login: 'editor1',
        firstname: 'John',
        lastname: 'Doe',
      };
      mockEditorService.getEditorByArticleId.mockResolvedValue(mockEditor);

      const result = await controller.getByarticleId(100);
      expect(result).toEqual(mockEditor);
      expect(mockEditorService.getEditorByArticleId).toHaveBeenCalledWith(100);
    });
  });
});
