import { FileUploader } from "react-drag-drop-files";
import axios from "axios";
import { showErrorNotification, showInfoNotification } from "./Main";

interface DragNDropProps {
    types: [string];
    url: string;
}

function DragNDrop({ types, url }: DragNDropProps) {

    const handleChange = async (file: any) => {
        const content = await fileToBase64(file);

        try {
            const response = await axios.post(
                url,
                {
                    fileName: file.name,
                    content: content
                },
                {
                    withCredentials: true,
                });

            if (response.status === 200) {
                showInfoNotification("Файл успешно загружен и импортирован.");
            } else {
                showErrorNotification("Не удалось импортировать файл.");
            }
        } catch (error) {
            console.error(error);
            showErrorNotification("Ошибка при импорте файла.");
        }
    };

    const fileToBase64 = (file: File): Promise<string> => {
        return new Promise((resolve, reject) => {
            const reader = new FileReader();
            reader.readAsDataURL(file);
            reader.onload = () => {
                const base64 = (reader.result as string).split(',')[1];
                resolve(base64);
            };
            reader.onerror = (error) => reject(error);
        });
    };

    return (
        <FileUploader
            handleChange={handleChange}
            name="file"
            types={types}
        />
    );
}

export default DragNDrop;
