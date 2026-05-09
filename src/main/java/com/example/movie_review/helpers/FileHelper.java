package com.example.movie_review.helpers;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import com.example.movie_review.models.UploadItem;

import lombok.extern.slf4j.Slf4j;
import net.coobird.thumbnailator.Thumbnails;
import net.coobird.thumbnailator.Thumbnails.Builder;
import net.coobird.thumbnailator.geometry.Position;
import net.coobird.thumbnailator.geometry.Positions;

/**
 * 파일 입출력을 위한 기능을 제공하는 클래스
 */
@Slf4j
@Component
public class FileHelper {

     //업로드 된 파일이 저장될 경로(application.properties로부터 읽어옴)
    @Value("${upload.dir}")
    private String uploadDir;
    // 업로드된 파일이 노출될 URL 경로 (application.properties로부터 읽어옴)
    @Value("${upload.url}")
    private String uploadUrl;

    @Value("${thumbnail.width}")
    private int thumbnailWidth;

    @Value("${thumbnail.height}")
    private int thumbnailHeight;

    @Value("${thumbnail.crop}")
    private boolean thumbnailCrop;
    
    /**
     * 파일에 데이터를 쓰는 메서드
     * @param filePath - 파일 경로
     * @param data - 저장할 데이터
     * @throws Exception - 파일 입출력 예외
     */
    public void write(String filePath, byte[] data) throws Exception {
        OutputStream os = null;
        try {
            // 저장할 파일 스트림 생성
            os = new FileOutputStream(filePath);
            // 파일 쓰기
            os.write(data);
        } catch (FileNotFoundException e) {
            log.error("파일을 찾을 수 없습니다.", e);
            throw e;
        } catch (IOException e) {
            log.error("파일을 쓸 수 없습니다.", e);
            throw e;
        } catch (Exception e) {
            log.error("파일 입출력 오류가 발생했습니다.", e);
            throw e;
        } finally {
            if (os != null) {
                try {
                    os.close();
                } catch (IOException e) {
                    log.error("파일을 닫는 중 오류가 발생했습니다.", e);
                    throw e;
                }
            } // end if
        } // try ~ catch ~ finally
    }

    /**
     * 파일에서 데이터를 읽는 메서드
     * @param filePath - 파일 경로
     * @return 파일에 저장된 데이터
     * @throws Exception - 파일 입출력 예외
     */
    public byte[] read(String filePath) throws Exception {
        byte[] data = null;

        InputStream is = null;
        try {
            is = new FileInputStream(filePath);
            data = new byte[is.available()];
            is.read(data);
        } catch (FileNotFoundException e) {
            log.error("파일을 찾을 수 없습니다.", e);
            throw e;
        } catch (IOException e) {
            log.error("파일을 읽을 수 없습니다.", e);
            throw e;
        } catch (Exception e) {
            log.error("파일 입출력 오류가 발생했습니다.", e);
            throw e;
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    log.error("파일을 닫는 중 오류가 발생했습니다.", e);
                    throw e;
                }
            }
        }

        return data;
    }

    /**
     * 파일에 문자열을 쓰는 메서드
     * @param filePath - 파일 경로
     * @param content - 저장할 문자열
     * @throws Exception - 파일 입출력 예외
     */
    public void writeString(String filePath, String content) throws Exception {
        try {
            this.write(filePath, content.getBytes("utf-8"));
        } catch (UnsupportedEncodingException e) {
            log.error("지원하지 않는 인코딩입니다.", e);
            throw e;
        }
    }

    /**
     * 파일에서 문자열을 읽는 메서드
     * @param filePath - 파일 경로
     * @return 파일에 저장된 문자열
     * @throws Exception - 파일 입출력 예외
     */
    public String readString(String filePath) throws Exception {
        String content = null;

        try {
            byte[] data = read(filePath);
            content = new String(data, "utf-8");
        } catch (Exception e) {
            log.error("지원하지 않는 인코딩입니다.", e);
            throw e;
        }

        return content;
    }

    /**
     * 컨트롤러부터 업로드된 파일의 정보를 전달받아 지정된 위치에 저장
     * 이때 파일 덮어 쓰기를 방지하기 위해 파일의 이름을 변경하고 이름 중복 검사를 수행
     * 
     * @param multipartFile 업로드된 파일 정보
     * @return 파일정보를 담고있는 객체
     */
    public UploadItem saveMultipartFile(MultipartFile multipartFile)throws NullPointerException, Exception{
        // 1. 업로드 파일 저장하기
        //  업로드 된 파일이 존재 유무 확인
        if (multipartFile.getOriginalFilename().isEmpty()) {
            throw new IllegalArgumentException("업로드 된 파일이 없습니다.");
        }
        
        //  업로드 된 파일의 정보 로그 기록
        log.debug("====================================");
        log.debug("원본 파일 이름 : "+multipartFile.getOriginalFilename());
        log.debug("input type=file 의 name 속성 값 : "+multipartFile.getName());
        log.debug("파일형식 : "+multipartFile.getContentType());
        log.debug("파일크기 : "+multipartFile.getSize());

        //  업로드 된 파일이 저장될 폴더의 이름을 년/월/일 형식으로 생성
        Calendar c = Calendar.getInstance();
        String targetDir = String.format("%s/%04d/%02d/%02d", uploadDir, c.get(Calendar.YEAR),c.get(Calendar.MONTH)+1,c.get(Calendar.DAY_OF_MONTH));
        
        //  폴더가 존재하지 않는다면 생성
        File f = new File(targetDir);
        if(!f.exists()){
            f.mkdirs();
        }
        //  저장될 파일의 이름생성
        //  파일의 원본 이름 추출
        String originName = multipartFile.getOriginalFilename();
        //  파일이 원본이름에서 확장자만 추출
        String ext = originName.substring(originName.lastIndexOf("."));
        String fileName = null; // 웹서버에 저장될 파일이름 변수 생성
        File targetFile = null; // 저장된 파일 정보를 담기 위한 File 객체
        int count = 0; // 중복된 이름을 갖는 파일 수를 카운트할 변수
        while (true) {
            // 저장될 파일 이름 --> 현재시각 + 카운트값 + 확장자
            fileName = String.format("%d%d%s", System.currentTimeMillis(),count,ext);
            // 업로드 파일이 "저장될 폴더 + 저장될 파일 이름"으로  파일 객체 생성
            targetFile = new File(targetDir,fileName);
            // 동일한 이름의 파일이 없다면 반복 중단
            if (!targetFile.exists()) {
                break;
            }
            // if문을 빠져 나올 경우 중복된 이름의 파일이 존재한다는 의미이므로 count를 1증가하고 반복 다시 수행
            count++;
        }
        //  업로드 된 파일을 지정된 경로로 복사
        multipartFile.transferTo(targetFile);

        // 2)업로드 경로 정보 처리하기
        //  백엔드상에 복사된 파일의 절대경로 추출
        //  --> 운영체제 호환(Window ->Linux) 를 위해 역슬래시를 슬래시로 변환
        String absPath = targetFile.getAbsolutePath().replace("\\", "/");
        log.debug("업로드 된 파일 경로 : "+absPath);

        //  업로드 된 파일의 절대경로에서 환경설정 파일에 명시된 폴더까지의 위츠는 삭제하여
        //  환경설정 파일에 명시된 upload.dir 이후의 위치만 추출(윈도우만)
        String filePath = null;
        if(absPath.substring(0,1).equals("/")){
            // Mac, Linux용 경로 처리
            filePath = absPath.replace(uploadDir,"");
        }else{
            // Window용 경로 처리 -> 설정 파일에 명시한 첫글자(/)를 제외하고 처리해야함
            filePath = absPath.replace(uploadDir.substring(1),"");
        }
        log.debug("업로드 폴더 내에서의 최종 경로 : "+filePath);

        // 3. 업로드  결과를 Beans에 저장
        UploadItem item = new UploadItem();
        item.setContentType(multipartFile.getContentType());
        item.setFiledName(multipartFile.getName());
        item.setFileSize(multipartFile.getSize());
        item.setOriginName(multipartFile.getOriginalFilename());
        item.setFilePath(filePath);

        //  업로드 경로를 웹상에서 접근 가능한 경로 문자열로 변환하여 Beans에 추가
        String fileUrl = String.format("%s%s", uploadUrl,filePath);
        item.setFileUrl(fileUrl);
        log.debug("파일의 URL : "+fileUrl);

        // 4. 파일 유형이 이미지라면 썸네일 생성
        if(item.getContentType().indexOf("image")> -1){
            try {
                String thumbnailPath = this.createThumbnail(filePath, thumbnailWidth, thumbnailHeight, thumbnailCrop);
                String thumbnailUrl = String.format("%s%s", uploadUrl, thumbnailPath);
                item.setThumbnailPath(thumbnailPath);
                item.setThumbnailUrl(thumbnailUrl);
            } catch (Exception e) {
               log.error("썸네일 생성에 실패함",e);
            }
        }
        log.debug("====================================");

        return item;

    }

    public List<UploadItem> saveMultipartFile(MultipartFile[] multipartFiles)
        throws NullPointerException, Exception{

        if(multipartFiles == null || multipartFiles.length ==0){
            throw new Exception("업로드 된 파일이 없습니다.");
        }

        List<UploadItem> uploadItemList = new ArrayList<>();
        for (MultipartFile file : multipartFiles) {
            UploadItem item = saveMultipartFile(file);
            uploadItemList.add(item);
        }
        return uploadItemList;
    }

    public String createThumbnail(String path, int width, int height, boolean crop) throws Exception{
        // 1. 썸네일 생성 정보 로그로 기록
        log.debug(String.format("[Thumbnail] path : %s, size : %d x %d, crop : %s",path, width,height, String.valueOf(crop)));

        // 2. 저장될 썸네일 이미지의 경로 문자열로 자르기
        File loadFile = new File(this.uploadDir,path); // 원본 파일의 전체경로 -> 업로드 폴더(상수값) + 파일명
        String dirPath = loadFile.getParent(); //전체 경로에서 파일이 위치한 폴더 경로 분리
        String fileName = loadFile.getName(); // 전체 경로에서 파일 이름만 분리
        int p = fileName.indexOf("."); 
        String name = fileName.substring(0,p);
        String ext = fileName.substring(p+1);

        // 최종 파일 이름 구성 --> 원본이름 +요청된 사이즈
        String thumbName = name+"_"+width+"x"+height+"."+ext;

        File f  = new File(dirPath, thumbName); // 생성될 썸네일 파일 객체 -> 업로드폴더 + 썸네일 이름
        String saveFile = f.getAbsolutePath(); // 생성될 썸네일 파일 객체로부터 절대 경로 추출(리턴할 값)

        // 생성될 썸네일 이미지의 경로를 로그로 기록
        log.debug(String.format("[Thumbnail] saveFile : %s",saveFile));

        // 3. 썸네일 이미지 생성하고 최종 경로 리턴
        //  해당 경로에 이미지가 없는 경우만 수행
        if(!f.exists()){
            Builder<File> builder = Thumbnails.of(loadFile);
            if (crop) {
                builder.crop(Positions.CENTER);
            }

            builder.size(width, height);
            builder.useExifOrientation(true);
            builder.outputFormat(ext);
            try {
                builder.toFile(saveFile);
            } catch (IOException e) {
                log.error("썸네일 이미지 생성 실패",e);
                throw e;
            }
        }

        // 윈도우와 Linux(Mac)에서 경로를 구분하는 문자열이 다르므로 "/" 로 통일
        String thumbnailPath = null;
        saveFile = saveFile.replace("\\", "/");
        if(saveFile.substring(0,1).equals("/")){
            // Mac, Linux용 경로 처리
            thumbnailPath = saveFile.replace(uploadDir,"");
        }else{
            // Window용 경로 처리 -> 설정 파일에 명시한 첫글자(/)를 제외하고 처리해야함
            thumbnailPath = saveFile.replace(uploadDir.substring(1),"");
        }

        return thumbnailPath;
    }

    public void deleteFile(String filePath) throws Exception{
        File file = new File(uploadDir, filePath);

        if(file.exists()){
            file.delete();
            log.debug("파일 삭제 성공 :{}",filePath);
        }

        String dirPath = file.getParent();
        String fileName = file.getName();
        int p = fileName.lastIndexOf(".");
        String name = fileName.substring(0,p);
        String ext = fileName.substring(p+1);
        String thumbName = name +"_"+thumbnailWidth+"x"+thumbnailHeight+"."+ext;

        File thumbFile = new File(dirPath, thumbName);
        String thumbPath = thumbFile.getAbsolutePath();

        if(thumbFile.exists()){
            thumbFile.delete();
            log.debug("썸네일 삭제 성공 :{}",thumbPath);
        }
    }
}